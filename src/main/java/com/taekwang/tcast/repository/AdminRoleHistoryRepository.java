package com.taekwang.tcast.repository;

import com.taekwang.tcast.model.entity.AdminRoleHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRoleHistoryRepository extends JpaRepository<AdminRoleHistory, Integer> {
}