package com.example.springbootsample.model.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    private String name;

    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;

    @Column(name = "updated_by", nullable = false, length = 50)
    private String updatedBy;

    @Builder
    public Channel(LocalDateTime createdAt, LocalDateTime updatedAt, Integer idx, String name, String createdBy, String updatedBy) {
        super(createdAt, updatedAt);
        this.idx = idx;
        this.name = name;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
}
