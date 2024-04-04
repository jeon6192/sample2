package com.taekwang.tcast.model.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Integer idx;

    @Column(name = "file_name", nullable = false, length = 200)
    private String fileName;

    @Column(name = "file_path", nullable = false, length = 300)
    private String filePath;

    @Column(name = "file_size", nullable = false)
    private Integer fileSize;

    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;

    @Column(name = "updated_by", nullable = false, length = 50)
    private String updatedBy;

    @Builder
    public Attachment(LocalDateTime createdAt, LocalDateTime updatedAt, Integer idx, String fileName, String filePath,
                      Integer fileSize, String createdBy, String updatedBy) {
        super(createdAt, updatedAt);
        this.idx = idx;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
}