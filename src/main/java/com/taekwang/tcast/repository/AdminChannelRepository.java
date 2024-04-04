package com.taekwang.tcast.repository;

import com.taekwang.tcast.model.entity.AdminChannel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminChannelRepository extends JpaRepository<AdminChannel, Integer> {
    List<AdminChannel> findByAdminIdx(Integer adminIdx);
}