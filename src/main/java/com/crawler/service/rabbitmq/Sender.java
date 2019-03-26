package com.crawler.service.rabbitmq;

import static com.crawler.config.RabbitMqConfig.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    @Autowired
    private RabbitTemplate template;

    public void send(String message) {
        template.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
        template.getConnectionFactory().createConnection().createChannel(true);
    }
}
