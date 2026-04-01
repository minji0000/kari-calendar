package com.kari.karicalender.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users") // user는 DB 예약어인 경우가 많아서 보통 users로 써요!
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 로그인 아이디 (unique 설정으로 중복 방지)
    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    // 본명
    @Column(nullable = false)
    private String realName;

    // 서비스 닉네임
    @Column(nullable = false, unique = true)
    private String nickname;

    // 전화번호 (중복 가입 방지)
    @Column(unique = true)
    private String phoneNumber;

    // 생년월일 (8자리: 19950527)
    @Column(length = 8)
    private String birthday;

    // 성별 ('F' 또는 'M' 한 글자만 저장)
    @Column(columnDefinition = "char(1)")
    private String gender;

    private String email;

    // 가입 경로 (LOCAL, KAKAO 등)
    private String provider;

    @Builder
    public User(String userId, String password, String realName, String nickname,
                String phoneNumber, String birthday, String gender, String email,
                String provider) {
        this.userId = userId;
        this.password = password;
        this.realName = realName;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.gender = gender;
        this.email = email;
        this.provider = provider;
    }
}