package com.taekwang.tcast.model.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {
    // common
    private String type;

    private Integer idx;

    private String id;

    private String password;

    private String name;

    private LocalDateTime withdrawalDate;

    // user
    private Map<String, Object> attributes;

    private String oauthType;

    private String oauthId;

    // admin
    private String role;

    private String allowedIp;

    private Integer loginFailureCnt;


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
    public CustomUserDetails(String type, Integer idx, String id, String password, String name, LocalDateTime withdrawalDate,
                             Map<String, Object> attributes, String role, String allowedIp, String oauthType,
                             String oauthId, Integer loginFailureCnt) {
        this.type = type;
        this.idx = idx;
        this.id = id;
        this.password = password;
        this.name = name;
        this.withdrawalDate = withdrawalDate;
        this.attributes = attributes;
        this.role = role;
        this.allowedIp = allowedIp;
        this.oauthType = oauthType;
        this.oauthId = oauthId;
        this.loginFailureCnt = loginFailureCnt;
    }
}
