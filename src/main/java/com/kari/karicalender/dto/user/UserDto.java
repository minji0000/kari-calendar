package com.kari.karicalender.dto.user;

import com.kari.karicalender.domain.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String userId;
    private String password;
    private String nickname;
    private String realName;
    private String email;
    private String phoneNumber;
    private String birthday;
    private String gender;
    private String provider;

    /**
     * DTO를 엔티티로 변환하는 메서드
     * 화면에서 입력받은 데이터를 기반으로 DB에 저장할 User 객체를 생성합니다.
     */
    public User toEntity(String encodedPassword) {
        return User.builder()
                .userId(userId)
                .password(encodedPassword) // 외부에서 암호화해서 던져준 값을 사용
                .nickname(nickname)
                .realName(realName)
                .email(email)
                .phoneNumber(phoneNumber)
                .birthday(birthday)
                .gender(gender)
                .provider(provider != null ? provider : "LOCAL")
                .build();
    }
}