package com.kari.karicalender.service;

import com.kari.karicalender.domain.User;
import com.kari.karicalender.dto.user.UserDto;
import com.kari.karicalender.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 설정해서 성능 최적화!
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

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

    /**
     * 로그인
     */
    public User login(String userId, String password) {
        return userRepository.findByUserId(userId)
                .filter(u -> u.getDeletedAt() == null)
                .filter(u -> u.getPassword().equals(password)) // 찾은 유저의 비번이 입력한 비번과 같은지 필터링!
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 맞지 않아요.")); // 다르면 에러!
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // 1. DB에서 아이디로 사용자를 찾고, 없으면 예외를 던집니다.
        com.kari.karicalender.domain.User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다: " + userId));

        // 2. 시큐리티가 인증을 진행할 수 있도록 UserDetails 객체로 변환해서 반환합니다.
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId())
                .password(user.getPassword()) // 암호화된 비밀번호
                .roles("USER")               // 권한 설정
                .build();
    }

}