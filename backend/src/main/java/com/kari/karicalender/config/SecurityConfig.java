package com.kari.karicalender.config;

import com.kari.karicalender.config.auth.LoginUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//스프링 시큐리티 설정파일
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginUserService loginUserService;

    //비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 로그인 하지 않은 사람도 볼 수 있는 설정
                        .requestMatchers("/", "/login", "/join", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/schedule/invite/**").authenticated()
                        .requestMatchers("/api/availability/**").authenticated()
                        //위에서 설정한 것 외에 모든 페이지는 로그인 해야 볼 수 있음
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        //권한 없는 사용자가 보호된 페이지 접근 시 보낼 주소
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        //로그인 시 아이디가 들어오는 input 태그의 name 속성값을 지정
                        .usernameParameter("userId")
                        .passwordParameter("password")
                        //로그인 성공 시 기본으로 보낼 페이지
                        //false는 "로그인 전에 가려던 곳이 있으면 거기로 보내주고, 없으면 메인으로 보내줘라"는 뜻
                        .defaultSuccessUrl("/schedule/main", false)
                        .permitAll()
                )
                .userDetailsService(loginUserService);

        return http.build();
    }
}