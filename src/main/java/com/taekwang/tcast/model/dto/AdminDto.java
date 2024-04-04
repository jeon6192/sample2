package com.taekwang.tcast.model.dto;

import com.taekwang.tcast.model.entity.Admin;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminDto {

    private Integer idx;

    private String id;

    @Setter
    private String password;

    private String name;

    private String email;

    private String phone;

    private String tel;

    private Integer deptIdx;

    private Integer roleIdx;

    private List<Integer> channelIdxList = new ArrayList<>();

    private String state;

    private String allowedIp;

    private Integer profileImageIdx;

    @Setter
    private LocalDateTime joinDate;

    private Integer loginFailureCnt;

    private LocalDateTime withdrawalDate;

    private LocalDateTime lastLoginDate;

    private String createdBy;

    private String updatedBy;

    @Builder
    public AdminDto(Integer idx, String id, String password, String name, String email, String phone, String tel, Integer deptIdx,
                    Integer roleIdx, List<Integer> channelIdxList, String state, String allowedIp, Integer profileImageIdx,
                    LocalDateTime joinDate, Integer loginFailureCnt, LocalDateTime withdrawalDate, LocalDateTime lastLoginDate, String createdBy, String updatedBy) {
        this.idx = idx;
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.tel = tel;
        this.deptIdx = deptIdx;
        this.roleIdx = roleIdx;
        this.channelIdxList = channelIdxList;
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

    public static AdminDto toDto(Admin admin) {
        return AdminDto.builder()
                .idx(admin.getIdx())
                .id(admin.getId())
                .password(admin.getPassword())
                .name(admin.getName())
                .email(admin.getEmail())
                .phone(admin.getPhone())
                .tel(admin.getTel())
                .deptIdx(admin.getDeptIdx())
                .state(admin.getState())
                .allowedIp(admin.getAllowedIp())
                .profileImageIdx(admin.getProfileImageIdx())
                .joinDate(admin.getJoinDate())
                .loginFailureCnt(admin.getLoginFailureCnt())
                .withdrawalDate(admin.getWithdrawalDate())
                .lastLoginDate(admin.getLastLoginDate())
                .createdBy(admin.getCreatedBy())
                .updatedBy(admin.getUpdatedBy())
                .build();
    }
}
