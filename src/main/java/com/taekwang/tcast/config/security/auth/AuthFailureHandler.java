package com.taekwang.tcast.config.security.auth;

import com.taekwang.tcast.model.dto.CustomOAuth2User;
import com.taekwang.tcast.model.enums.UserError;
import com.taekwang.tcast.util.CommonUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        // 회원테이블에 데이터가 없을 경우 회원가입 페이지로 이동
        if (exception instanceof OAuth2AuthenticationException oAuth2AuthenticationException && "NOT FOUND USER".equals(oAuth2AuthenticationException.getError().getErrorCode())) {
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) request.getSession().getAttribute("customOAuth2User");
            String oauthType = (String) request.getSession().getAttribute("oauthType");
            String name = customOAuth2User.getName();
            // TODO: 카카오는 비즈앱 신청 후 이름을 가져올 수 있는 권한신청이 가능하므로 추후 제거
            if ("kakao".equals(oauthType)) {
                name = "kakao";
            }
            String redirectUrl = "/signup?oauthType=" + oauthType + "&name=" + URLEncoder.encode(name, StandardCharsets.UTF_8) + "&oauthId=" + customOAuth2User.getOAuthId();

            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } else {
            UserError userError = UserError.getErrorByAuthenticationEx(exception);

            CommonUtil.printErrorMessage(response, userError);
        }
    }
}
