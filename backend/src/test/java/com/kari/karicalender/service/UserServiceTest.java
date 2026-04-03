package com.kari.karicalender.service;

import com.kari.karicalender.domain.User;
import com.kari.karicalender.repository.UserRepository;
import com.kari.karicalender.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
@SpringBootTest
@Transactional //DB에 실제 저장 X 테스트 끝나면 자동으로 삭제(Rollback) 됨
public class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void join_success() {
        // Given (준비)
        User user = User.builder()
                .userId("goguma")
                .password("test")
                .nickname("고구마")
                .realName("진민지")
                .build();

        // When (실행)
        Long savedId = userService.join(user);

        // Then (검증)
        User findUser = userRepository.findById(savedId).get();
        assertThat(findUser.getUserId()).isEqualTo("goguma");
    }

    @Test
    @DisplayName("중복 아이디 가입 시 예외가 발생해야 한다")
    void join_duplicate_exception() {
        // Given
        User user1 = User.builder().userId("sameId").password("p1").nickname("n1").build();
        User user2 = User.builder().userId("sameId").password("p2").nickname("n2").build();

        // When
        userService.join(user1);

        // Then
        // 에러가 발생하는 게 정상적인 결과
        assertThrows(IllegalStateException.class, () -> {
            userService.join(user2); // 아이디가 같아서 에러가 터져야 함!
        });
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void login_success() {
        // Given
        User user = User.builder().userId("goguma").password("test").nickname("nick").build();
        userService.join(user);

        // When
        User loggedInUser = userService.login("goguma", "test");

        // Then
        assertThat(loggedInUser.getNickname()).isEqualTo("nick");
    }
}
