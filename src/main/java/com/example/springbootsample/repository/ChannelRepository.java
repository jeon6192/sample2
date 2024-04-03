package com.example.springbootsample.repository;

import com.example.springbootsample.model.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Integer> {
}