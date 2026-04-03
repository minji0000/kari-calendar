package com.kari.karicalender.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 테스트 단계에선 일단 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/join", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login") // 우리가 만든 로그인 페이지 주소
                        .defaultSuccessUrl("/", true) // 로그인 성공하면 갈 곳
                        .permitAll()
                );

        return http.build();
    }
}