package com.taekwang.tcast.model.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonCode extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    private String category;

    private String code;

    private Boolean isActive;

    private String createdBy;

    private String updatedBy;

    @Builder
    public CommonCode(LocalDateTime createdAt, LocalDateTime updatedAt, Integer idx, String category, String code,
                      Boolean isActive, String createdBy, String updatedBy) {
        super(createdAt, updatedAt);
        this.idx = idx;
        this.category = category;
        this.code = code;
        this.isActive = isActive;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
}