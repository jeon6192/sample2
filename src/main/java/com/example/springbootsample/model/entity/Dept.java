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
public class Dept extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @OneToOne
    @JoinColumn(name = "parent_idx", referencedColumnName = "idx")
    private Dept parent;

    private String name;

    private String createdBy;

    private String updatedBy;

    @Builder
    public Dept(LocalDateTime createdAt, LocalDateTime updatedAt, Integer idx, Dept parent, String name, String createdBy, String updatedBy) {
        super(createdAt, updatedAt);
        this.idx = idx;
        this.parent = parent;
        this.name = name;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
}
