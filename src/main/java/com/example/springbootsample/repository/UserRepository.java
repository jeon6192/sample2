package com.example.springbootsample.repository;

import com.example.springbootsample.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String id);

    Optional<User> findByOauthTypeAndOauthId(String oauthType, String oauthId);

    boolean existsById(String id);
}
