package com.example.springbootsample.controller;

import com.example.springbootsample.model.dto.AdminDto;
import com.example.springbootsample.model.entity.Channel;
import com.example.springbootsample.model.entity.Dept;
import com.example.springbootsample.model.entity.Role;
import com.example.springbootsample.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
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
    public ResponseEntity<Map<String, Object>> registerAdmin(@ModelAttribute AdminDto adminDto) {
        AdminDto dto = adminService.registerAdmin(adminDto);
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
        return ResponseEntity.ok(adminService.findAllRoles());
    }

    // 부서목록 조회
    @GetMapping("/dept")
    public ResponseEntity<List<Dept>> getDeptList() {
        return ResponseEntity.ok(adminService.findAllDepts());
    }

    // 채널목록 조회
    @GetMapping("/channel")
    public ResponseEntity<List<Channel>> getChannelList() {
        return ResponseEntity.ok(adminService.findAllChannels());
    }

}
