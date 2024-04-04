package com.taekwang.tcast.service;

import com.taekwang.tcast.exception.UserException;
import com.taekwang.tcast.model.dto.UserDto;
import com.taekwang.tcast.model.entity.User;
import com.taekwang.tcast.model.enums.OAuth2Type;
import com.taekwang.tcast.model.enums.UserError;
import com.taekwang.tcast.repository.UserRepository;
import com.taekwang.tcast.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
        User user = userRepository.save(User.toEntity(dto));

        return UserDto.toDto(user);
    }

    public UserDto registerUser(UserDto dto) throws UserException {
        if (!dto.isOAuth()) {
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            dto.setPassword(encodedPassword);
        }

        try {
            return saveUser(dto);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new UserException(UserError.BAD_REQUEST);
        }
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
