package com.example.springbootsample.config.security;

import com.example.springbootsample.model.dto.ErrorResponse;
import com.example.springbootsample.model.enums.UserError;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Spring Server 에 요청 시 미인증 상태인 경우 핸들링 하여 응답 포맷 지정
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(UserError.PERMISSION_DENIED);

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType jsonMimeType = MediaType.APPLICATION_JSON;

        if (jsonConverter.canWrite(errorResponse.getClass(), jsonMimeType)) {
            jsonConverter.write(errorResponse, jsonMimeType, new ServletServerHttpResponse(response));
        }
    }

}
