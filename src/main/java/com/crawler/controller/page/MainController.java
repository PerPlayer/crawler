package com.crawler.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    @RequestMapping({"/", "index.html"})
    public String index(){
        return "index";
    }

    @RequestMapping("main.html")
    public String main(){
        return "main";
    }

    @RequestMapping("menu.html")
    public String menu(){
        return "menu";
    }

    @RequestMapping("task.html")
    public String task(){
        return "task";
    }
}
