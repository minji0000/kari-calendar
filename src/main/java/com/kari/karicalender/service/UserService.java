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

        User user = userDto.toEntity(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
        return user.getId();
    }

    /**
     * 회원가입 시 사용자 정보 중복체크
     */
    private void validateDuplicateUser(UserDto userDto) {
        // 1. 아이디 중복 체크 (필수)
        if (userRepository.existsByUserId(userDto.getUserId())) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        // 2. 닉네임 중복 체크 (필수)
        if (userRepository.existsByNickname(userDto.getNickname())) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }

        // 3. 이메일 중복 체크 (선택 입력)
        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new IllegalStateException("이미 등록된 이메일입니다.");
            }
        }
    }

}