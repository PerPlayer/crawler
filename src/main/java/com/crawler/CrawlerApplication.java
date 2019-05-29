package com.crawler;

import com.crawler.config.MybatisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
//@Profile("dev")
public class CrawlerApplication extends SpringBootServletInitializer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    private static ConfigurableApplicationContext applicationContext = null;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(CrawlerApplication.class, args);
    }

    public static void exit(){
        SpringApplication.exit(applicationContext, () -> {return 0;});
    }

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        factory.setPort(80);
    }

    //可执行war包
    @Override
    public SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CrawlerApplication.class);
    }
}
