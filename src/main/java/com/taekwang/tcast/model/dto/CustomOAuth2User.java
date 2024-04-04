package com.taekwang.tcast.model.dto;

import com.taekwang.tcast.model.enums.OAuth2Type;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User extends DefaultOAuth2User {

    private String name;

    private String email;

    private String oauthId;

    @Builder
    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, String name, String email, String oauthId) {
        super(authorities, attributes, nameAttributeKey);
        this.name = name;
        this.email = email;
        this.oauthId = oauthId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getOAuthId() {
        return this.oauthId;
    }

    public static CustomOAuth2User of(OAuth2Type oAuth2Type, Map<String, Object> attributes, String nameAttributeKey) {
        return switch (oAuth2Type) {
            case GOOGLE -> CustomOAuth2User.builder()
                    .name((String) attributes.get("name"))
                    .email((String) attributes.get("email"))
                    .oauthId((String) attributes.get("sub"))
                    .attributes(attributes)
                    .nameAttributeKey(nameAttributeKey)
                    .build();
            case NAVER -> {
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                yield CustomOAuth2User.builder()
                        .name((String) response.get("name"))
                        .email((String) response.get("email"))
                        .oauthId((String) response.get("id"))
                        .attributes(attributes)
                        .nameAttributeKey(nameAttributeKey)
                        .build();
            }
            case KAKAO -> {
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                yield CustomOAuth2User.builder()
                        .name((String) kakaoAccount.get("name"))
                        .email((String) kakaoAccount.get("email"))
                        .oauthId(String.valueOf(attributes.get("id")))
                        .attributes(attributes)
                        .nameAttributeKey(nameAttributeKey)
                        .build();
            }
        };

    }
}
