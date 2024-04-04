package com.taekwang.tcast.repository;

import com.taekwang.tcast.model.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Integer> {
}