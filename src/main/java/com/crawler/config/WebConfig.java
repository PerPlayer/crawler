package com.crawler.config;

import com.crawler.extend.HandlerExceptionResolverExtend;
import com.crawler.extend.InjectCurrentUserHandler;
import com.crawler.listener.StartTaskApplicationListener;
import com.crawler.util.ApplicationContextUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Created by SHW on 2019/3/3.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(InjectCurrentUserHandler());
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
