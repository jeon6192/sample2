package com.example.springbootsample.model.dto;

import com.example.springbootsample.exception.UserException;
import com.example.springbootsample.model.enums.UserError;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private String errorCode;
    private String message;
    private String detail;

    @Builder
    public ErrorResponse(String errorCode, String message, String detail) {
        this.errorCode = errorCode;
        this.message = message;
        this.detail = detail;
    }

    public static ErrorResponse of(UserError userError) {
        return ErrorResponse.builder()
                .errorCode(userError.getErrorCode())
                .message(userError.getMessage())
                .build();
    }

    public static ErrorResponse of(UserError userError, UserException e) {
        return ErrorResponse.builder()
                .errorCode(userError.getErrorCode())
                .message(userError.getMessage())
                .detail(e.getDetail())
                .build();
    }
}
