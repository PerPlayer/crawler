package com.crawler.service.impl;

import com.crawler.annonation.GroupValid;
import com.crawler.crawler.model.CodeEntry;
import com.crawler.dao.CodeEntryDao;
import com.crawler.dao.MenuDao;
import com.crawler.entity.MenuEntity;
import com.crawler.repository.CodeEntryRepository;
import com.crawler.repository.MenuRepository;
import com.crawler.service.CacheService;
import com.crawler.service.CodeEntryService;
import com.crawler.service.MenuService;
import com.crawler.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuImpl implements MenuService {

    @Autowired
    private MenuRepository repository;

    @Autowired
    private MenuDao dao;

    @Autowired
    private CacheService cacheService;

    @Override
    public MenuEntity save(@GroupValid MenuEntity menuEntity) {
        EntityUtil.init(menuEntity);
        return repository.save(menuEntity);
    }

    @Override
    public void update(String id, String column, Object value) {
        dao.update(id, column, value);
    }

    @Override
    public void update(MenuEntity menuEntity) {
        MenuEntity oldEntry = repository.findById(menuEntity.getId()).get();
        menuEntity.setVersion(oldEntry.getVersion());
        cacheService.put(menuEntity.getId(), menuEntity);
        repository.save(menuEntity);
    }

    @Override
    public void delete(MenuEntity menuEntity) {
        repository.delete(menuEntity);
    }

    @Override
    public List<MenuEntity> findAll() {
        List<MenuEntity> menus = repository.findAll();
        for (MenuEntity entity : menus) {
            if (entity.getType() == 0) {
                entity.setChilds(repository.findByParentId(entity.getId()));
            }
        }
        return menus;
    }

    @Override
    public Page<MenuEntity> findAll(Pageable page) {
        return repository.findAll(page);

    }
    public List<MenuEntity> findByParentId(String parentId){
        Object obj = cacheService.get(parentId);
        if(obj != null){
            return (List<MenuEntity>)obj;
        }
        List<MenuEntity> menuEntitys = repository.findByParentId(parentId);
        cacheService.put(parentId, menuEntitys);
        return menuEntitys;
    }
}
