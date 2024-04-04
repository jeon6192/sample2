package com.taekwang.tcast.config.security.auth;

import com.taekwang.tcast.model.dto.CustomUserDetails;
import com.taekwang.tcast.model.dto.UserDto;
import com.taekwang.tcast.model.entity.User;
import com.taekwang.tcast.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;

    public AuthSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인된 유저객체 얻어오기
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String oauthType = customUserDetails.getOauthType();
        String oauthId = customUserDetails.getOauthId();

        User user = userService.findByOauthTypeAndOauthId(oauthType, oauthId).orElseThrow(()
                -> new UsernameNotFoundException("USER NOT FOUND"));

        // 로그인에 성공 시 마지막 로그인 시간 갱신
        UserDto userDto = UserDto.toDto(user);
        userDto.setOAuth(true);

        userService.saveUser(userDto);

        // TODO: 로그인 이력 테이블에 insert

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
