package com.taekwang.tcast.service;

import com.taekwang.tcast.exception.UserException;
import com.taekwang.tcast.model.dto.UserDto;
import com.taekwang.tcast.model.entity.User;
import com.taekwang.tcast.model.entity.UserAccessHistory;
import com.taekwang.tcast.model.enums.OAuth2Type;
import com.taekwang.tcast.model.enums.UserError;
import com.taekwang.tcast.repository.UserAccessHistoryRepository;
import com.taekwang.tcast.repository.UserRepository;
import com.taekwang.tcast.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserAccessHistoryRepository userAccessHistoryRepository;
    private final UserAgentAnalyzer userAgentAnalyzer;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserAccessHistoryRepository userAccessHistoryRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userAccessHistoryRepository = userAccessHistoryRepository;
        this.userAgentAnalyzer = UserAgentAnalyzer.newBuilder().build();
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

    public void saveUserAccessHistory(HttpServletRequest request, User user) {
        String accessIp = request.getHeader("X-Forwarded-For");
        if (!StringUtils.hasLength(accessIp)) {
            accessIp = request.getRemoteAddr();
        }
        String referer = request.getHeader("Referer");
        String userAgentString = request.getHeader("User-Agent");
        if (StringUtils.hasLength(userAgentString)) {
            UserAgent userAgent = userAgentAnalyzer.parse(userAgentString);
            userAgentString = userAgent.getValue("DeviceName") +
                    " / " +
                    userAgent.getValue("OperatingSystemNameVersion") +
                    " / " +
                    userAgent.getValue("AgentNameVersion");
        }

        UserAccessHistory userAccessHistory = UserAccessHistory.builder()
                .userIdx(user.getIdx())
                .loginTime(LocalDateTime.now())
                .accessIp(accessIp)
                .deviceInfo(userAgentString)
                .referer(referer)
                .build();

        userAccessHistoryRepository.save(userAccessHistory);
    }
}
