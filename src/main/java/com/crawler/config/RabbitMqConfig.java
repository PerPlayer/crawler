package com.crawler.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE_NAME = "exchange_spring_demo";
    public static final String QUEUE_NAME = "queue_spring_demo";
    public static final String ROUTING_KEY = "routing_key_spring";

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue queue(){
        return new Queue(QUEUE_NAME, true, false, false);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(ROUTING_KEY);
    }
}
