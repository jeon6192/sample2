package com.taekwang.tcast.config.security.user;

import com.taekwang.tcast.model.dto.CustomUserDetails;
import com.taekwang.tcast.model.entity.User;
import com.taekwang.tcast.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // AuthenticationProvider 에서 받은 파라미터 (username) 를 통하여 DB 조회
        User user = userRepository.findById(username).orElseThrow(()
                -> new UsernameNotFoundException("USER NOT FOUND"));

        // UserDetails 를 상속하여 만든 CustomUserDetails 를 user 객체의 정보를 추가하여 생성 후 리턴
        return CustomUserDetails.builder()
                .id(user.getId())
                .password(user.getPassword())
                .withdrawalDate(user.getWithdrawalDate())
                .build();
    }
}