package com.taekwang.tcast.config.security.user;

import com.taekwang.tcast.model.dto.ErrorResponse;
import com.taekwang.tcast.model.enums.UserError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    public UserAuthenticationFailureHandler() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        /*
        스프링 시큐리티 AuthenticationException 종류
        - UsernameNotFoundException : 계정 없음
        - BadCredentialsException : 비밀번호 미일치
        - AccountStatusException
            - AccountExpiredException : 계정만료
            - CredentialsExpiredException : 비밀번호 만료
            - DisabledException : 계정 비활성화
            - LockedException : 계정 잠김
        */
        UserError userError = UserError.getErrorByAuthenticationEx(exception);

        printErrorMessage(response, userError);
    }

    private void printErrorMessage(HttpServletResponse response, @NonNull UserError userError) throws IOException {
        int statusCode;

        if (userError.getHttpStatus() != null) {
            statusCode = userError.getHttpStatus().value();
        } else {
            statusCode = HttpStatus.UNAUTHORIZED.value();
        }

        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = ErrorResponse.of(userError);

        String errorJson = objectMapper.writeValueAsString(errorResponse);
        PrintWriter writer = response.getWriter();
        writer.println(errorJson);
    }
}
