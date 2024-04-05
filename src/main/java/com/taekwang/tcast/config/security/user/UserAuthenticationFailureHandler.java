package com.taekwang.tcast.config.security.user;

import com.taekwang.tcast.model.enums.UserError;
import com.taekwang.tcast.util.CommonUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

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

        CommonUtil.printErrorMessage(response, userError);
    }
}
