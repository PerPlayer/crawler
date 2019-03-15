package com.crawler.service;

import com.crawler.entity.LogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LogService {

    LogEntity save(LogEntity logEntity);

    void delete(LogEntity logEntity);

    List<LogEntity> findAll();

    Page<LogEntity> findAll(Pageable page);
}
