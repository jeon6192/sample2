package com.taekwang.tcast.controller;

import com.taekwang.tcast.exception.UserException;
import com.taekwang.tcast.model.dto.CustomUserDetails;
import com.taekwang.tcast.model.dto.UserDto;
import com.taekwang.tcast.model.entity.User;
import com.taekwang.tcast.model.enums.UserError;
import com.taekwang.tcast.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public ModelAndView navigateToSignup(@RequestParam(required = false) String oauthType, @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String oauthId) {
        ModelAndView modelAndView = new ModelAndView("signup");
        modelAndView.addObject("isOAuth", oauthType != null);

        if (oauthType != null) {
            String id = userService.generateUserId(oauthType);
            modelAndView.addObject("id", id);
            modelAndView.addObject("name", name);
            modelAndView.addObject("oauthType", oauthType);
            modelAndView.addObject("oauthId", oauthId);
        }

        return modelAndView;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> saveUser(@ModelAttribute UserDto dto) throws UserException {
        return ResponseEntity.ok(userService.registerUser(dto));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> aboutMe(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws UserException {
        String oauthType = customUserDetails.getOauthType();

        UserDto userDto;

        if (StringUtils.hasLength(oauthType)) {
            String oauthId = customUserDetails.getOauthId();

            User user = userService.findByOauthTypeAndOauthId(oauthType, oauthId)
                    .orElseThrow(() -> new UserException(UserError.NOT_FOUND_USER));

            userDto = UserDto.toDto(user);
        } else {
            String userId = customUserDetails.getUsername();
            User user = userService.findByUserId(userId)
                    .orElseThrow(() -> new UserException(UserError.NOT_FOUND_USER));

            userDto = UserDto.toDto(user);
        }

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/login")
    public ModelAndView navigateToLogin() {
        return new ModelAndView("login");
    }
}
