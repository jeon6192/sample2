package com.taekwang.tcast.config.security.admin;

import com.taekwang.tcast.model.dto.CustomUserDetails;
import com.taekwang.tcast.model.entity.Admin;
import com.taekwang.tcast.model.entity.Role;
import com.taekwang.tcast.repository.AdminRepository;
import com.taekwang.tcast.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    private final RoleRepository roleRepository;

    public CustomAdminUserDetailsService(AdminRepository adminRepository, RoleRepository roleRepository) {
        this.adminRepository = adminRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // AuthenticationProvider 에서 받은 파라미터 (username) 를 통하여 DB 조회
        Admin admin = adminRepository.findById(username).orElseThrow(()
                -> new UsernameNotFoundException("USER NOT FOUND"));

        Integer roleIdx = admin.getRoleIdx();
        Role role = roleRepository.findById(roleIdx).orElse(null);
        String roleName = role == null ? null : role.getName();

        // UserDetails 를 상속하여 만든 CustomUserDetails 를 user 객체의 정보를 추가하여 생성 후 리턴
        return CustomUserDetails.builder()
                .id(admin.getId())
                .password(admin.getPassword())
                .withdrawalDate(admin.getWithdrawalDate())
                .role(roleName)
                .allowedIp(admin.getAllowedIp())
                .loginFailureCnt(admin.getLoginFailureCnt())
                .build();
    }
}