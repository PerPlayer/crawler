package com.crawler.listener;

import com.crawler.crawler.Crawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class StartTaskApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    Logger logger = LoggerFactory.getLogger(StartTaskApplicationListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("------- 任务启动 ------");
        new Thread(){
            public void run() {
                try {
//                    Crawler.main(null);
                } catch (Exception e) {
                    logger.error("启动任务异常：{}", e.getMessage());
                }
            }
        }.start();
    }
}
