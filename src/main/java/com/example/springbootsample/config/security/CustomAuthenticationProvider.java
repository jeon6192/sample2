package com.example.springbootsample.config.security;

import com.example.springbootsample.model.dto.CustomUserDetails;
import com.example.springbootsample.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, customUserDetails.getPassword())) {
            throw new BadCredentialsException("패스워드가 일치하지 않습니다.");
        }

        if (!customUserDetails.isEnabled()) {
            throw new DisabledException("탈퇴한 회원 입니다.");
        }

        return new UsernamePasswordAuthenticationToken(customUserDetails, password, customUserDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AnonymousAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
