package com.crawler.listener;

import com.crawler.crawler.Crawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class StartTaskApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    Logger logger = LoggerFactory.getLogger(StartTaskApplicationListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            logger.info("------- 任务启动 ------");
            Crawler.main(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
