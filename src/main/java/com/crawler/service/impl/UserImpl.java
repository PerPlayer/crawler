package com.crawler.service.impl;

import com.crawler.entity.UserEntity;
import com.crawler.repository.UserRepository;
import com.crawler.service.UserService;
import com.crawler.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserEntity save(UserEntity userEntity) {
        EntityUtil.init(userEntity);
        return repository.save(userEntity);
    }

    @Override
    public void delete(UserEntity userEntity) {
        repository.delete(userEntity);
    }

    @Override
    public List<UserEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<UserEntity> findAll(Pageable page) {
        return repository.findAll(page);
    }
}
