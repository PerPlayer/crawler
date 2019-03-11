package com.crawler.repository;

import com.crawler.crawler.model.Task;
import com.crawler.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuEntity, String> {

    List<MenuEntity> findByParentId(String parentId);
}
