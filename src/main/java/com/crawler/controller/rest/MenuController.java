package com.crawler.controller.rest;

import com.crawler.entity.MenuEntity;
import com.crawler.service.MenuService;
import com.crawler.util.B;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService service;

    @RequestMapping("/query/all")
    public B queryAll(){
        List<MenuEntity> menus = service.findAll();
        return B.ok().put("menuList", menus);
    }

    @RequestMapping("/query/list")
    public List<MenuEntity> queryList(){
        List<MenuEntity> menus = service.findAll();
        return menus;
    }
}
