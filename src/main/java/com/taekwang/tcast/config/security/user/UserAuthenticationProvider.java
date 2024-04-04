package com.taekwang.tcast.config.security.user;

import com.taekwang.tcast.model.dto.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    public UserAuthenticationProvider(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, customUserDetails.getPassword())) {
            throw new BadCredentialsException("password");
        }

        if (!customUserDetails.isEnabled()) {
            throw new DisabledException("탈퇴한 회원 입니다.");
        }

        return new UsernamePasswordAuthenticationToken(customUserDetails, password, customUserDetails.getAuthorities());
    }

    /**
     * UsernamePasswordAuthenticationToken: 사용자 이름과 비밀번호로부터 생성되는 인증 토큰.
     * JwtAuthenticationToken: JWT 토큰 기반의 인증 토큰.
     * OAuth2AuthenticationToken: OAuth2 프로토콜을 사용하는 인증 토큰.
     * RememberMeAuthenticationToken: "Remember Me" 기능을 통해 생성되는 인증 토큰.
     * AnonymousAuthenticationToken: 인증되지 않은 익명 사용자를 나타내는 인증 토큰.
     * PreAuthenticatedAuthenticationToken: 사전 인증된 사용자를 나타내는 인증 토큰.
     * @return 지원하는 token 일 경우 true 반환
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return AnonymousAuthenticationToken.class.isAssignableFrom(authentication)
                || UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)
                || OAuth2AuthenticationToken.class.isAssignableFrom(authentication)
                || RememberMeAuthenticationToken.class.isAssignableFrom(authentication)
                ;
    }
}
