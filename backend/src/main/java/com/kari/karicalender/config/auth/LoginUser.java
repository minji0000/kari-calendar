package com.kari.karicalender.config.auth;

import com.kari.karicalender.domain.User;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class LoginUser implements UserDetails {

    private final User user; // 우리 DB의 User 엔티티

    public LoginUser(User user) {
        this.user = user;
    }

    // 권한 설정: 최소한의 성의 표시 (일반 유저 권한)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "ROLE_USER");
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId(); // 로그인 아이디 컬럼인 userId 반환
    }

    // 계정 상태 체크: 전부 true로 해서 시큐리티 통과시키기
    //계정 만료 여부
    @Override
    public boolean isAccountNonExpired() { return true; }

    //isAccountNonLocked() (계정 잠금 여부)
    @Override
    public boolean isAccountNonLocked() { return true; }

    //isCredentialsNonExpired() (비밀번호 만료)
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    //isEnabled() (계정 활성화 여부)
    @Override
    public boolean isEnabled() { return true; }
}