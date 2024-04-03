package com.example.springbootsample.model.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentDto implements Serializable {
    private Integer id;

    private String fileName;

    private String filePath;

    private Integer fileSize;

    @Builder
    public AttachmentDto(Integer id, String fileName, String filePath, Integer fileSize) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
}