package com.crawler.controller.rest;

import com.crawler.crawler.model.Task;
import com.crawler.service.EntryService;
import com.crawler.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService service;

    @GetMapping("/query/list")
    public List<Task> list(){
        Page<Task> taskPage = service.findAll(PageRequest.of(1, 3));
        return taskPage.getContent();
    }
}
