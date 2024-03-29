package com.example.springbootsample.model.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum UserError {
    BAD_REQUEST("001", "요청 값 오류", HttpStatus.BAD_REQUEST),
    PERMISSION_DENIED("002", "인증되지 않음", UNAUTHORIZED),
    HAVE_NO_DATA("003", "데이터 없음", NOT_FOUND),
    FORBIDDEN("004", "권한 없음", HttpStatus.FORBIDDEN),
    NOT_FOUND_USER("005", "계정 없음", UNAUTHORIZED),
    MISMATCH_PASSWORD("006", "비밀번호 불일치", UNAUTHORIZED),
    WITHDRAWAL_USER("008", "탈퇴한 계정", UNAUTHORIZED),
    NOT_DEFINED("999", "서버 에러", INTERNAL_SERVER_ERROR),
    ;

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;

    UserError(String errorCode, String message, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public static UserError getErrorByAuthenticationEx(AuthenticationException exception) {
        UserError userError;

        if (exception instanceof BadCredentialsException) {
            userError = MISMATCH_PASSWORD;
        } else if (exception instanceof UsernameNotFoundException) {
            userError = NOT_FOUND_USER;
        } else if (exception instanceof DisabledException) {
            userError = WITHDRAWAL_USER;
        } else {
            userError = NOT_DEFINED;
        }

        return userError;
    }
}
