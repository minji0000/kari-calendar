package com.kari.karicalender.config;

import com.kari.karicalender.config.auth.LoginUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginUserService loginUserService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 설정 추가: /api/로 시작하는 요청은 CSRF 검사를 하지 않음
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                .authorizeHttpRequests(auth -> auth
                        // 2. API 경로에 대한 권한 설정 (로그인한 사용자만 가능)
                        .requestMatchers("/api/availability/**").authenticated()
                        .requestMatchers("/", "/login", "/join", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("userId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/schedule/main", true)
                        .permitAll()
                )
                .userDetailsService(loginUserService);

        return http.build();
    }
}