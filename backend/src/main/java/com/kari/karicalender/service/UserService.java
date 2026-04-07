package com.kari.karicalender.service;

import com.kari.karicalender.domain.User;
import com.kari.karicalender.dto.user.UserDto;
import com.kari.karicalender.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 설정해서 성능 최적화!
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(UserDto userDto) {
        validateDuplicateUser(userDto);

        // 엔티티로 변환할 때 암호화된 비밀번호를 바로 넣어주기
        User user = User.builder()
                .userId(userDto.getUserId())
                .password(passwordEncoder.encode(userDto.getPassword())) // 여기서 바로 암호화!
                .nickname(userDto.getNickname())
                .provider("LOCAL")
                // ... 나머지 필드들 ...
                .build();

        userRepository.save(user);
        return user.getId();
    }

    /**
     * 아이디 & 닉네임 중복 체크
     */
    private void validateDuplicateUser(UserDto userDto) {
        if (userRepository.existsByUserId(userDto.getUserId())) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
        if (userRepository.existsByNickname(userDto.getNickname())) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }

    /**
     * 로그인
     */
    public User login(String userId, String password) {
        return userRepository.findByUserId(userId)
                .filter(u -> u.getDeletedAt() == null)
                .filter(u -> u.getPassword().equals(password)) // 찾은 유저의 비번이 입력한 비번과 같은지 필터링!
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 맞지 않아요.")); // 다르면 에러!
    }
}