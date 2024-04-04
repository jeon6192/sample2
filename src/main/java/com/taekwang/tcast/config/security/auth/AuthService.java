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

        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession(true);
        OAuth2Type oAuth2Type = OAuth2Type.findByType(registrationId);

        CustomOAuth2User customOAuth2User = CustomOAuth2User.of(oAuth2Type, attributes, nameAttributeKey);
        String oauthId = customOAuth2User.getOAuthId();

        Optional<User> optionalUser = userService.findByOauthTypeAndOauthId(registrationId, oauthId);
        if (optionalUser.isEmpty()) {
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
