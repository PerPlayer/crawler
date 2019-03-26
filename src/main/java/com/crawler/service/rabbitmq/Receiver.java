package com.crawler.service.rabbitmq;

import com.crawler.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
public class Receiver{

    @RabbitHandler
    public void process(String message){
        System.out.println(message);
    }
}
