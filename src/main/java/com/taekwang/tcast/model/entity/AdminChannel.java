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
public class AdminChannel extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    private Integer adminIdx;

    private Integer channelIdx;

    private String createdBy;

    private String updatedBy;

    @Builder
    public AdminChannel(LocalDateTime createdAt, LocalDateTime updatedAt, Integer idx, Integer adminIdx, Integer channelIdx, String createdBy, String updatedBy) {
        super(createdAt, updatedAt);
        this.idx = idx;
        this.adminIdx = adminIdx;
        this.channelIdx = channelIdx;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
}