package com.taekwang.tcast.controller;

import com.taekwang.tcast.annotation.CurrentUser;
import com.taekwang.tcast.exception.UserException;
import com.taekwang.tcast.model.dto.AdminDto;
import com.taekwang.tcast.model.dto.CommonCodeDto;
import com.taekwang.tcast.model.dto.CustomUserDetails;
import com.taekwang.tcast.model.entity.Channel;
import com.taekwang.tcast.model.entity.CommonCode;
import com.taekwang.tcast.model.entity.Dept;
import com.taekwang.tcast.model.entity.Role;
import com.taekwang.tcast.model.enums.UserError;
import com.taekwang.tcast.service.AdminService;
import com.taekwang.tcast.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class AdminController {

    private final AdminService adminService;

    private final CommonService commonService;

    public AdminController(AdminService adminService, CommonService commonService) {
        this.adminService = adminService;
        this.commonService = commonService;
    }

    @GetMapping("/admin/login")
    public ModelAndView navigateToAdminLogin() {
        return new ModelAndView("admin/login");
    }

    @GetMapping("/admin/home")
    public ModelAndView navigateToAdminHome() {
        return new ModelAndView("admin/home");
    }

    @PostMapping("/admin/register")
    public ResponseEntity<Map<String, Object>> registerAdmin(@CurrentUser CustomUserDetails customUserDetails, @ModelAttribute AdminDto adminDto) throws UserException {
        boolean isMaster = adminService.isMaster(customUserDetails.getIdx());
        if (!isMaster) {
            throw new UserException(UserError.FORBIDDEN);
        }

        AdminDto dto = adminService.registerAdmin(adminDto, customUserDetails);
        Map<String, Object> result = adminService.getAdminByIdx(dto.getIdx());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/admin/{adminIdx}")
    public ResponseEntity<Map<String, Object>> getAdminInfo(@PathVariable Integer adminIdx) {
        return ResponseEntity.ok(adminService.getAdminByIdx(adminIdx));
    }

    // 권한목록 조회
    @GetMapping("/role")
    public ResponseEntity<List<Role>> getRoleList() {
        return ResponseEntity.ok(commonService.findAllRoles());
    }

    // 부서목록 조회
    @GetMapping("/dept")
    public ResponseEntity<List<Dept>> getDeptList() {
        return ResponseEntity.ok(commonService.findAllDepts());
    }

    // 채널목록 조회
    @GetMapping("/channel")
    public ResponseEntity<List<Channel>> getChannelList() {
        return ResponseEntity.ok(commonService.findAllChannels());
    }

    // 공통 코드 조회
    @GetMapping("/code")
    public ResponseEntity<List<CommonCodeDto>> getCodeList(@RequestParam(required = false) String category,
                                                           @RequestParam(required = false) String code,
                                                           @RequestParam(required = false) Boolean isActive) {
        return ResponseEntity.ok(commonService.findAllCommonCodes(category, code, isActive));
    }

}
