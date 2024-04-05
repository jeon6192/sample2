package com.taekwang.tcast.model.dto;

import com.taekwang.tcast.model.entity.Attachment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttachmentDto implements Serializable {
    private Integer idx;

    private String fileName;

    private String filePath;

    private Integer fileSize;

    @Builder
    public AttachmentDto(Integer idx, String fileName, String filePath, Integer fileSize) {
        this.idx = idx;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public static AttachmentDto toDto(Attachment entity) {
        return AttachmentDto.builder()
                .idx(entity.getIdx())
                .fileName(entity.getFileName())
                .filePath(entity.getFilePath())
                .fileSize(entity.getFileSize())
                .build();
    }
}