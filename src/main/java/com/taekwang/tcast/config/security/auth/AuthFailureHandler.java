package com.taekwang.tcast.config.security.auth;

import com.taekwang.tcast.model.dto.CustomOAuth2User;
import com.taekwang.tcast.model.dto.ErrorResponse;
import com.taekwang.tcast.model.enums.UserError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        // 회원테이블에 데이터가 없을 경우 회원가입 페이지로 이동
        if (exception instanceof OAuth2AuthenticationException) {
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) request.getSession().getAttribute("customOAuth2User");
            String oauthType = (String) request.getSession().getAttribute("oauthType");
            String name = customOAuth2User.getName();
            if ("kakao".equals(oauthType)) {
                name = "kakao";
            }
            String redirectUrl = "/signup?oauthType=" + oauthType + "&name=" + URLEncoder.encode(name, StandardCharsets.UTF_8) + "&oauthId=" + customOAuth2User.getOAuthId();

            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } else {
            UserError userError = UserError.getErrorByAuthenticationEx(exception);

            printErrorMessage(response, userError);
        }
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
