package com.taekwang.tcast.config.security.admin;

import com.taekwang.tcast.model.dto.CustomUserDetails;
import com.taekwang.tcast.model.entity.Admin;
import com.taekwang.tcast.service.AdminService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AdminService adminService;

    public AdminAuthenticationSuccessHandler(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인된 유저객체 얻어오기
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String id = customUserDetails.getUsername();

        Admin admin = adminService.findByAdminId(id).orElseThrow(()
                -> new UsernameNotFoundException("USER NOT FOUND"));

        // 로그인에 성공 시 마지막 로그인 시간 갱신
        admin.updateLoginTime();
        // 로그인 실패 카운트 초기화
        admin.setLoginFailureCnt(0);

        adminService.saveAdmin(admin);

        super.setDefaultTargetUrl("/admin/home");

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
