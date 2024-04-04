package com.taekwang.tcast.config.security.admin;

import com.taekwang.tcast.model.dto.ErrorResponse;
import com.taekwang.tcast.model.entity.Admin;
import com.taekwang.tcast.model.enums.UserError;
import com.taekwang.tcast.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    private final AdminService adminService;

    public AdminAuthenticationFailureHandler(AdminService adminService) {
        this.adminService = adminService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        // 비밀번호 오입력 시 로그인 실패횟수 카운트 업데이트
        if (exception instanceof BadCredentialsException) {
            String id = request.getParameter("username");
            Admin admin = adminService.findByAdminId(id).orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));

            admin.setLoginFailureCnt(admin.getLoginFailureCnt() + 1);

            adminService.saveAdmin(admin);
        } else if (exception instanceof LockedException) {
            // TODO: 비밀번호 초기화 후 이메일 발송

        }

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
