package com.crawler.controller.page;

import com.crawler.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("element/")
public class EntryController {

    @Autowired
    private EntryService service;

    @GetMapping("index/")
    public String index(){
        service.save(null);
        return "index";
    }
}
