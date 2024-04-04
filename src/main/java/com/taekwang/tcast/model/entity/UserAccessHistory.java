package com.taekwang.tcast.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAccessHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    private Integer userIdx;

    private LocalDateTime loginTime;

    private LocalDateTime logoutTime;

    private String referer;

    private String accessIp;

    private String deviceInfo;

    @Builder
    public UserAccessHistory(Integer idx, Integer userIdx, LocalDateTime loginTime, LocalDateTime logoutTime, String referer, String accessIp, String deviceInfo) {
        this.idx = idx;
        this.userIdx = userIdx;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.referer = referer;
        this.accessIp = accessIp;
        this.deviceInfo = deviceInfo;
    }
}