package com.kari.karicalender.config.auth;

import com.kari.karicalender.domain.User;
import com.kari.karicalender.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserService implements UserDetailsService {

    private final UserRepository userRepository;

    // 스프링 시큐리티가 로그인 폼에서 받은 'userId'를 이 메서드의 파라미터로 던져줍니다.
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        // 1. DB에서 해당 아이디를 가진 유저가 있는지 찾습니다.
        User userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 아이디를 찾을 수 없습니다: " + userId));

        // 2. 찾은 유저 엔티티를 우리가 만든 'LoginUser'에 담아서 반환합니다!
        // 여기서 반환된 LoginUser가 시큐리티 세션에 쏙 들어갑니다.
        return new LoginUser(userEntity);
    }
}
