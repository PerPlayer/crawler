package com.crawler.config;

import com.crawler.extend.HandlerExceptionResolverExtend;
import com.crawler.extend.InjectCurrentUserHandler;
import com.crawler.listener.StartTaskApplicationListener;
import com.crawler.util.ApplicationContextUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * Created by SHW on 2019/3/3.
 */
@Configuration
@ComponentScan("com.crawler.controller")
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(InjectCurrentUserHandler());
    }

    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(messageConverter());
    }

    /**
     * 静态资源处理
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/statics/**")
                .addResourceLocations("classpath:/statics/");
    }

    // json转换器
    @Bean
    public MappingJackson2HttpMessageConverter messageConverter() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("application", "json", Charset.forName("utf-8"))));
        return messageConverter;
    }

    @Bean
    public InjectCurrentUserHandler InjectCurrentUserHandler(){
        return new InjectCurrentUserHandler();
    }

    @Bean
    public HandlerExceptionResolverExtend HandlerExceptionResolverExtend(){
        return new HandlerExceptionResolverExtend();
    }

    @Bean
    public LocalValidatorFactoryBean LocalValidatorFactoryBean(){
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setProviderClass(org.hibernate.validator.HibernateValidator.class);
        validatorFactoryBean.setValidationMessageSource(ResourceBundleMessageSource());
        return validatorFactoryBean;
    }

    @Bean
    public ResourceBundleMessageSource ResourceBundleMessageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("classpath:ValidationMessages.properties");//如果不加默认使用classpath下的 ValidationMessages.properties
        return messageSource;
    }

    @Bean
    public StartTaskApplicationListener StartTaskApplicationListener(){
        return new StartTaskApplicationListener();
    }

    @Bean
    public ApplicationContextUtil ApplicationContextUtil(){
        return new ApplicationContextUtil();
    }
}
