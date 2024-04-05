package com.taekwang.tcast.model.dto;

import com.taekwang.tcast.model.entity.CommonCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonCodeDto {
    private Integer idx;
    private String category;
    private String code;
    private Boolean isActive;

    @Builder
    public CommonCodeDto(Integer idx, String category, String code, Boolean isActive) {
        this.idx = idx;
        this.category = category;
        this.code = code;
        this.isActive = isActive;
    }

    public static CommonCodeDto toDto(CommonCode code) {
        return CommonCodeDto.builder()
                .idx(code.getIdx())
                .category(code.getCategory())
                .code(code.getCode())
                .isActive(code.getIsActive())
                .build();
    }

    public static List<CommonCodeDto> toDtoList(List<CommonCode> list) {
        return list.stream()
                .map(CommonCodeDto::toDto)
                .toList();
    }
}