package com.crawler.service;

import com.crawler.crawler.model.CodeEntry;
import com.crawler.entity.MenuEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuService {

    MenuEntity save(MenuEntity menuEntity);

    void update(String id, String column, Object value);

    void update(MenuEntity menuEntity);

    void delete(MenuEntity menuEntity);

    List<MenuEntity> findAll();

    Page<MenuEntity> findAll(Pageable page);

    List<MenuEntity> findByParentId(String parentId);
}
