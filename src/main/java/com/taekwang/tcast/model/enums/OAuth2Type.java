package com.taekwang.tcast.model.enums;

import lombok.Getter;

@Getter
public enum OAuth2Type {
    GOOGLE("google", "GG"), NAVER("naver", "NV"), KAKAO("kakao", "KK")
    ;

    private final String oauthType;

    private final String idPrefix;

    OAuth2Type(String oauthType, String idPrefix) {
        this.oauthType = oauthType;
        this.idPrefix = idPrefix;
    }

    public static OAuth2Type findByType(String oAuth2Type) {
        for (OAuth2Type value : values()) {
            if (value.oauthType.equals(oAuth2Type)) {
                return value;
            }
        }
        return null;
    }
}
