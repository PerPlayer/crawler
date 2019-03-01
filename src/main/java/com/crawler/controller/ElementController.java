package com.crawler.controller;

import com.crawler.service.ElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller("element")
public class ElementController {

    @Autowired
    private ElementService service;

    public String list(){
        service.save(null);
        return "list";
    }
}
