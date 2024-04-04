package com.taekwang.tcast.repository;

import com.taekwang.tcast.model.entity.UserAccessHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccessHistoryRepository extends JpaRepository<UserAccessHistory, Integer> {
}