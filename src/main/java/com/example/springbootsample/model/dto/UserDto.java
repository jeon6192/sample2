package com.example.springbootsample.model.dto;

import com.example.springbootsample.model.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    private Long idx;

    private String id;

    @Setter
    private String password;

    private String name;

    private String email;

    private String birth;

    private String phone;

    private String gender;

    private String nickname;

    private String oauthType;

    private String oauthId;

    @Setter
    private boolean isOAuth;

    private LocalDateTime lastLoginTime;

    @Builder
    public UserDto(Long idx, String id, String password, String name, String email, String birth,
                   String phone, String gender, String nickname, String oauthType, String oauthId, boolean isOAuth, LocalDateTime lastLoginTime) {
        this.idx = idx;
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.phone = phone;
        this.gender = gender;
        this.nickname = nickname;
        this.oauthType = oauthType;
        this.oauthId = oauthId;
        this.isOAuth = isOAuth;
        this.lastLoginTime = lastLoginTime;
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .idx(user.getIdx())
                .id(user.getId())
                .password(user.getPassword())
                .name(user.getName())
                .email(user.getEmail())
                .birth(user.getBirth())
                .phone(user.getPhone())
                .gender(user.getGender())
                .nickname(user.getNickname())
                .oauthType(user.getOauthType())
                .oauthId(user.getOauthId())
                .lastLoginTime(user.getLastLoginTime())
                .build();
    }

}
