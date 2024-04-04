package com.taekwang.tcast.config.security.user;

import com.taekwang.tcast.model.dto.CustomUserDetails;
import com.taekwang.tcast.model.entity.User;
import com.taekwang.tcast.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;

    public UserAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인된 유저객체 얻어오기
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String id = customUserDetails.getUsername();

        User user = userService.findByUserId(id).orElseThrow(()
                -> new UsernameNotFoundException("USER NOT FOUND"));

        // TODO: 로그인 이력 테이블에 insert

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
