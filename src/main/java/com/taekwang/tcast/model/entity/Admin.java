package com.taekwang.tcast.model.entity;

import com.taekwang.tcast.model.dto.AdminDto;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    private String id;

    private String password;

    private String name;

    private String email;

    private String phone;

    private String tel;

    private Integer deptIdx;

    private Integer roleIdx;

    private String state;

    private String allowedIp;

    private Integer profileImageIdx;

    private LocalDateTime joinDate;

    @Setter
    private Integer loginFailureCnt;

    private LocalDateTime withdrawalDate;

    private LocalDateTime lastLoginDate;

    private String createdBy;

    private String updatedBy;

    @Builder
    public Admin(LocalDateTime createdAt, LocalDateTime updatedAt, Integer idx, String id, String password, String name,
                 String email, String phone, String tel, Integer deptIdx, Integer roleIdx, String state, String allowedIp,
                 Integer profileImageIdx, LocalDateTime joinDate, Integer loginFailureCnt, LocalDateTime withdrawalDate, LocalDateTime lastLoginDate,
                 String createdBy, String updatedBy) {
        super(createdAt, updatedAt);
        this.idx = idx;
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.tel = tel;
        this.deptIdx = deptIdx;
        this.roleIdx = roleIdx;
        this.state = state;
        this.allowedIp = allowedIp;
        this.profileImageIdx = profileImageIdx;
        this.joinDate = joinDate;
        this.loginFailureCnt = loginFailureCnt;
        this.withdrawalDate = withdrawalDate;
        this.lastLoginDate = lastLoginDate;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public void updateLoginTime() {
        this.lastLoginDate = LocalDateTime.now();
    }

    public static Admin toEntity(AdminDto dto) {
        return Admin.builder()
                .idx(dto.getIdx())
                .id(dto.getId())
                .password(dto.getPassword())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .tel(dto.getTel())
                .deptIdx(dto.getDeptIdx())
                .roleIdx(dto.getRoleIdx())
                .state(dto.getState())
                .allowedIp(dto.getAllowedIp())
                .profileImageIdx(dto.getProfileImageIdx())
                .joinDate(dto.getJoinDate())
                .loginFailureCnt(dto.getLoginFailureCnt())
                .withdrawalDate(dto.getWithdrawalDate())
                .lastLoginDate(dto.getLastLoginDate())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }
}
