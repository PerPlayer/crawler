package com.crawler.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class MybatisConfig {

    @Bean
    public SqlSessionFactoryBean SqlSessionFactoryBean(DataSource dataSource) throws IOException {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourceResolver.getResources("conf/mapper/*.xml");
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
//        factoryBean.setMapperLocations(new Resource[]{new DefaultResourceLoader().getResource("classpath:conf/mapper")});
        factoryBean.setConfigLocation(new ClassPathResource("conf/mybatis-config.xml"));
        factoryBean.setMapperLocations(resources);
        return factoryBean;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setSqlSessionFactoryBeanName("SqlSessionFactoryBean");
        configurer.setBasePackage("com.crawler.dao");
        return configurer;
    }

}
