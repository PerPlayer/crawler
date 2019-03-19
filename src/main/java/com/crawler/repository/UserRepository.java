package com.crawler.repository;

import com.crawler.entity.LogEntity;
import com.crawler.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    UserEntity findByUsername(String username);
}
