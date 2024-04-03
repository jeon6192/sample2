package com.example.springbootsample.model.entity;

import com.example.springbootsample.model.dto.UserDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@DynamicInsert
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    private String id;

    private String password;

    private String name;

    private String email;

    private String birth;

    private String phone;

    private String gender;

    private String nickname;

    private String oauthType;

    private String oauthId;

    private LocalDateTime withdrawalDate;

    private LocalDateTime passwordModifiedDate;

    @Builder
    public User(Integer idx, String id, String password, String name, String email, String birth, String phone,
                String gender, String nickname, String oauthType, String oauthId, LocalDateTime withdrawalDate,
                LocalDateTime passwordModifiedDate) {
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
        this.withdrawalDate = withdrawalDate;
        this.passwordModifiedDate = passwordModifiedDate;
    }

    public void updateWithdrawalDate(LocalDateTime withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    public static User toEntity(UserDto dto) {
        return User.builder()
                .idx(dto.getIdx())
                .id(dto.getId())
                .password(dto.getPassword())
                .name(dto.getName())
                .email(dto.getEmail())
                .birth(dto.getBirth())
                .phone(dto.getPhone())
                .gender(dto.getGender())
                .birth(dto.getBirth())
                .oauthType(dto.getOauthType())
                .oauthId(dto.getOauthId())
                .passwordModifiedDate(dto.getPasswordModifiedDate())
                .build();
    }
}
