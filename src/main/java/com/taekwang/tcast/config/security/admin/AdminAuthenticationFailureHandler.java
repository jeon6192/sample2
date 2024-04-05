package com.taekwang.tcast.config.security.admin;

import com.taekwang.tcast.model.entity.Admin;
import com.taekwang.tcast.model.enums.UserError;
import com.taekwang.tcast.service.AdminService;
import com.taekwang.tcast.service.MailService;
import com.taekwang.tcast.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AdminAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final AdminService adminService;

    private final MailService mailService;

    public AdminAuthenticationFailureHandler(AdminService adminService, MailService mailService) {
        this.adminService = adminService;
        this.mailService = mailService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String id = request.getParameter("username");

        // 비밀번호 오입력 시 로그인 실패횟수 카운트 업데이트
        if (exception instanceof BadCredentialsException) {
            Admin admin = adminService.findByAdminId(id).orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));

            admin.setLoginFailureCnt(admin.getLoginFailureCnt() + 1);
            log.debug("Admin {} Login Failure Count :: {}", admin.getName(), admin.getLoginFailureCnt());

            adminService.saveAdmin(admin);
        } else if (exception instanceof LockedException) {
            // 비밀번호 초기화 후 이메일 발송
            Admin admin = adminService.findByAdminId(id).orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
            String email = admin.getEmail();
            if (!StringUtils.hasLength(email)) {
                throw new UsernameNotFoundException("EMAIL INFO NOT FOUND");
            }

            String newPassword = CommonUtil.generatePassword();
            log.debug("newPassword : {}", newPassword);
            String subject = "New Password";
            String text = "New Password : " + newPassword + "\n변경된 비밀번호로 로그인 해주세요";

            mailService.sendMail(email, subject, text);
            log.debug("Send Mail");
            log.debug("Send Mail To : {} / Subject : {} / Text : {}", email, subject, text);

            String encodedPassword = adminService.encodePassword(newPassword);
            log.debug("encodedPassword : {}", encodedPassword);

            admin.setPassword(encodedPassword);

            adminService.saveAdmin(admin);
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

        CommonUtil.printErrorMessage(response, userError);
    }
}
