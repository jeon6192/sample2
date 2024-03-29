package com.example.springbootsample.service;

import com.example.springbootsample.model.dto.UserDto;
import com.example.springbootsample.model.entity.User;
import com.example.springbootsample.model.enums.OAuth2Type;
import com.example.springbootsample.repository.UserRepository;
import com.example.springbootsample.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public Optional<User> findByUserId(String username) {
        return userRepository.findById(username);
    }

    public UserDto saveUser(UserDto dto) {
        if (!dto.isOAuth()) {
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            dto.setPassword(encodedPassword);
        }

        User user = userRepository.save(User.toEntity(dto));

        return UserDto.toDto(user);
    }

    public Optional<User> findByOauthTypeAndOauthId(String oauthType, String oauthId) {
        return userRepository.findByOauthTypeAndOauthId(oauthType, oauthId);
    }

    public String generateUserId(String oauthType) {
        String prefix = OAuth2Type.findByType(oauthType).getIdPrefix();
        String randomNumber = Integer.toString(CommonUtil.generateRandomNumber(6));

        String userId = prefix + randomNumber;

        boolean exists = userRepository.existsById(userId);
        if (exists) {
            return generateUserId(oauthType);
        }

        return prefix + randomNumber;
    }
}
