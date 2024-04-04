package com.taekwang.tcast.service;

import com.taekwang.tcast.mapper.AdminMapper;
import com.taekwang.tcast.model.dto.AdminDto;
import com.taekwang.tcast.model.entity.*;
import com.taekwang.tcast.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final DeptRepository deptRepository;
    private final ChannelRepository channelRepository;
    private final AdminChannelRepository adminChannelRepository;
    private final AdminMapper adminMapper;


    public AdminService(PasswordEncoder passwordEncoder, AdminRepository adminRepository, RoleRepository roleRepository, DeptRepository deptRepository,
                        ChannelRepository channelRepository, AdminChannelRepository adminChannelRepository, AdminMapper adminMapper) {
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
        this.roleRepository = roleRepository;
        this.deptRepository = deptRepository;
        this.channelRepository = channelRepository;
        this.adminChannelRepository = adminChannelRepository;
        this.adminMapper = adminMapper;
    }

    public Optional<Admin> findByAdminId(String id) {
        return adminRepository.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public AdminDto registerAdmin(AdminDto requestDto) {
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        requestDto.setPassword(encodedPassword);
        requestDto.setJoinDate(LocalDateTime.now());

        Admin admin = saveAdmin(Admin.toEntity(requestDto));
        Integer adminIdx = admin.getIdx();

        List<Integer> channelIdxList = requestDto.getChannelIdxList();
        for (Integer channelIdx : channelIdxList) {
            AdminChannel adminChannel = AdminChannel.builder()
                    .adminIdx(adminIdx)
                    .channelIdx(channelIdx)
                    .build();

            adminChannelRepository.save(adminChannel);
        }

        return AdminDto.toDto(admin);
    }

    public Map<String, Object> getAdminByIdx(int adminIdx) {
        Map<String, Object> result = adminMapper.selectAdminByIdx(adminIdx);
        List<Map<String, Object>> channelList = adminMapper.selectChannelListByAdminIdx(adminIdx);
        result.put("channelList", channelList);

        return result;
    }

    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public List<Dept> findAllDepts() {
        return deptRepository.findAll();
    }

    public List<Channel> findAllChannels() {
        return channelRepository.findAll();
    }

    public List<AdminChannel> findAdminChannelsByAdminIdx(Integer adminIdx) {
        return adminChannelRepository.findByAdminIdx(adminIdx);
    }
}
