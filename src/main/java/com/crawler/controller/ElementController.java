package com.crawler.controller;

import com.crawler.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller("element")
public class ElementController {

    @Autowired
    private EntryService service;

    public String list(){
        service.save(null);
        return "list";
    }
}
