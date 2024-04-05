package com.taekwang.tcast.repository;

import com.taekwang.tcast.model.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommonCodeRepository extends JpaRepository<CommonCode, Integer> {
    List<CommonCode> findByCategoryAndCodeAndIsActive(String category, String code, Boolean isActive);
}