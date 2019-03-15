package com.crawler.service.impl;

import com.crawler.entity.LogEntity;
import com.crawler.entity.MenuEntity;
import com.crawler.repository.LogRepository;
import com.crawler.service.LogService;
import com.crawler.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogImpl implements LogService {

    @Autowired
    private LogRepository repository;

    @Override
    public LogEntity save(LogEntity logEntity) {
        EntityUtil.init(logEntity);
        return repository.save(logEntity);
    }

    @Override
    public void delete(LogEntity logEntity) {
        repository.delete(logEntity);
    }

    @Override
    public List<LogEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<LogEntity> findAll(Pageable page) {
        return repository.findAll(page);
    }
}
