package com.taekwang.tcast.config.security.auth;

import com.taekwang.tcast.model.dto.CustomOAuth2User;
import com.taekwang.tcast.model.dto.CustomUserDetails;
import com.taekwang.tcast.model.entity.User;
import com.taekwang.tcast.model.enums.OAuth2Type;
import com.taekwang.tcast.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AuthService extends DefaultOAuth2UserService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String nameAttributeKey = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Type oAuth2Type = OAuth2Type.findByType(registrationId);

        CustomOAuth2User customOAuth2User = CustomOAuth2User.of(oAuth2Type, attributes, nameAttributeKey);
        String oauthId = customOAuth2User.getOAuthId();

        // 소셜 타입 (registrationId) 와 아이디 (oauthId) 로 user 테이블 조회
        Optional<User> optionalUser = userService.findByOauthTypeAndOauthId(registrationId, oauthId);
        // 회원정보가 없다면 회원가입 페이지로 리다이렉트 시키기 위해 Exception 발생
        if (optionalUser.isEmpty()) {
            // FailureHandler 에 데이터 전달을 위해 session 사용
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession(true);
            session.setAttribute("oauthType", oAuth2Type.getOauthType());
            session.setAttribute("customOAuth2User", customOAuth2User);

            throw new OAuth2AuthenticationException("NOT FOUND USER");
        }

        User user = optionalUser.get();

        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .type("USER")
                .idx(user.getIdx())
                .id(user.getId())
                .name(user.getName())
                .withdrawalDate(user.getWithdrawalDate())
                .oauthType(user.getOauthType())
                .oauthId(user.getOauthId())
                .build();

        if (!customUserDetails.isEnabled()) {
            throw new DisabledException("탈퇴한 회원 입니다.");
        }

        return customUserDetails;
    }

}
