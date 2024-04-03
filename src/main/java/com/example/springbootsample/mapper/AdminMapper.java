package com.example.springbootsample.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {
    Map<String, Object> selectAdminByIdx(Integer adminIdx);

    List<Map<String, Object>> selectChannelListByAdminIdx(Integer adminIdx);
}
