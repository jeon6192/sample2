package com.taekwang.tcast.service;

import com.taekwang.tcast.model.dto.CommonCodeDto;
import com.taekwang.tcast.model.entity.*;
import com.taekwang.tcast.repository.ChannelRepository;
import com.taekwang.tcast.repository.CommonCodeRepository;
import com.taekwang.tcast.repository.DeptRepository;
import com.taekwang.tcast.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommonService {
    private final RoleRepository roleRepository;
    private final DeptRepository deptRepository;
    private final ChannelRepository channelRepository;
    private final CommonCodeRepository commonCodeRepository;

    public CommonService(RoleRepository roleRepository, DeptRepository deptRepository, ChannelRepository channelRepository,
                         CommonCodeRepository commonCodeRepository) {
        this.roleRepository = roleRepository;
        this.deptRepository = deptRepository;
        this.channelRepository = channelRepository;
        this.commonCodeRepository = commonCodeRepository;
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

    public List<CommonCodeDto> findAllCommonCodes(String category, String code, Boolean isActive) {
        List<CommonCode> commonCodeList = commonCodeRepository.findByCategoryAndCodeAndIsActive(category, code, isActive);

        return CommonCodeDto.toDtoList(commonCodeList);
    }
}
