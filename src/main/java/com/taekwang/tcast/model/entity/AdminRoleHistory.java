package com.taekwang.tcast.model.entity;

import com.taekwang.tcast.model.enums.RoleActionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminRoleHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    private Integer adminIdx;

    private Integer roleIdx;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum")
    private RoleActionType actionType;

    private String createdBy;

    private String updatedBy;

    @Builder
    public AdminRoleHistory(LocalDateTime createdAt, LocalDateTime updatedAt, Integer idx, Integer adminIdx, Integer roleIdx,
                            RoleActionType actionType, String createdBy, String updatedBy) {
        super(createdAt, updatedAt);
        this.idx = idx;
        this.adminIdx = adminIdx;
        this.roleIdx = roleIdx;
        this.actionType = actionType;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
}