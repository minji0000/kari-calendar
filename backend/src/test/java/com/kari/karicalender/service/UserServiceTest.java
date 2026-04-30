package com.kari.karicalender.service;

import com.kari.karicalender.domain.User;
import com.kari.karicalender.dto.user.UserDto;
import com.kari.karicalender.repository.UserRepository;
import com.kari.karicalender.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = com.kari.karicalender.KariCalenderApplication.class)
@Transactional
public class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공 테스트 (암호화 확인)")
    void join_success() {
        // Given (DTO를 사용해서 준비!)
        UserDto dto = new UserDto();
        dto.setUserId("goguma");
        dto.setPassword("test1234");
        dto.setNickname("고구마");

        // When
        Long savedId = userService.join(dto);

        // Then
        User findUser = userRepository.findById(savedId).get();
        assertThat(findUser.getUserId()).isEqualTo("goguma");
        // 중요: DB에는 'test1234'가 아니라 암호화된 외계어가 들어있어야 함!
        assertThat(findUser.getPassword()).isNotEqualTo("test1234");
    }

    @Test
    @DisplayName("로그인 성공 테스트 (암호화 비교 확인)")
    void login_success() {
        // Given
        UserDto dto = new UserDto();
        dto.setUserId("goguma");
        dto.setPassword("test1234"); // 원래 비번
        dto.setNickname("고구마");
        userService.join(dto);

        // When
        // 서비스 내부에서 passwordEncoder.matches()가 작동해야 성공함!
        User loggedInUser = userService.login("goguma", "test1234");

        // Then
        assertThat(loggedInUser.getUserId()).isEqualTo("goguma");
        assertThat(loggedInUser.getNickname()).isEqualTo("고구마");
    }

    @Test
    @DisplayName("비밀번호가 틀리면 로그인에 실패해야 한다")
    void login_fail() {
        // Given
        UserDto dto = new UserDto();
        dto.setUserId("goguma");
        dto.setPassword("correct_password");
        dto.setNickname("고구마");
        userService.join(dto);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.login("goguma", "wrong_password");
        });
    }
}