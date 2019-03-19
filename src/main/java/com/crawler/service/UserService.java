package com.crawler.service;

import com.crawler.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserEntity save(UserEntity userEntity);

    void delete(UserEntity userEntity);

    List<UserEntity> findAll();

    Page<UserEntity> findAll(Pageable page);
}
