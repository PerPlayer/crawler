package com.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
//@Profile("dev")
public class CrawlerApplication implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    public static void main(String[] args) {
        SpringApplication.run(CrawlerApplication.class, args);
    }

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        factory.setPort(80);
    }
}
