package com.example.springbootsample.model.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomUserDetails implements UserDetails, OAuth2User {

    private final String id;

    private final String password;

    private final String name;

    private final LocalDateTime withdrawalDate;

    private final Map<String, Object> attributes;

    @Getter
    private final String oauthType;

    @Getter
    private final String oauthId;

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.withdrawalDate == null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Builder
    public CustomUserDetails(String id, String password, String name, LocalDateTime withdrawalDate, Map<String, Object> attributes, String oauthType, String oauthId) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.withdrawalDate = withdrawalDate;
        this.attributes = attributes;
        this.oauthType = oauthType;
        this.oauthId = oauthId;
    }

}
