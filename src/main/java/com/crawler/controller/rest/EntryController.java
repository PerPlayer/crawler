package com.crawler.controller.rest;

import com.crawler.crawler.model.Entry;
import com.crawler.service.EntryService;
import com.crawler.util.BPage;
import com.crawler.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entry")
public class EntryController {

    @Autowired
    private EntryService service;

    @GetMapping("/query")
    public List<Entry> list(@RequestParam("search") String search){
        Page<Entry> taskPage = service.findAllByContent(search, PageRequest.of(1, 10));
        return taskPage.getContent();
    }

    @PostMapping("/query/page")
    public BPage<Entry> list(@RequestBody Query query){
        BPage page = query.getPage();
        Page<Entry> taskPage = service.findAllByContent(query.getContent(), PageRequest.of(page.getCurrent(), page.getSize()));
        page.setList(taskPage.getContent());
        page.setTotal(taskPage.getTotalElements()/page.getSize());
        return page;
    }
}
