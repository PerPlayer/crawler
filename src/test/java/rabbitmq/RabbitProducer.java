package rabbitmq;

import com.rabbitmq.client.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RabbitProducer {

    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "47.101.189.129";
    private static final int PORT = 5672;

    private static Connection connection;

    @Before
    public void createConnection() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("guest");
        factory.setPassword("guest");
        connection = factory.newConnection();
    }

    @Test
    public void sendTest() throws Exception {
        Channel channel = connection.createChannel();
        initMQ(channel);
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 10000);//设置消息队列的过期时间，单位：毫秒
        channel.queueDeclare(QUEUE_NAME, true, false, false, args);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        String message = "你好!";
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println("done...");
        channel.close();
        connection.close();
    }

    @Test
    public void sendWithListenerTest() throws Exception {
        // 现有交换器已经声明了备份交换器，备份交换顺优先级高于mandatory，要重新声明新的交换器，监听才起作用
        Channel channel = connection.createChannel();
        channel.addReturnListener((int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) -> {
                    System.out.println("back msg: " + new String(body));
                }
        );
        String message = "你好!";
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY+"2",true, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        TimeUnit.SECONDS.sleep(3);
        System.out.println("done...");
        channel.close();
        connection.close();
    }

    @Test
    public void sendWithAETest() throws Exception {
        Channel channel = connection.createChannel();
        HashMap<String, Object> args = new HashMap<>();
        args.put("alternate-exchange", "exchange_ae");
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, args);
        channel.exchangeDeclare("exchange_ae", "fanout", true, false, null);
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueDeclare("queue_ae", true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        channel.queueBind("queue_ae", "exchange_ae", ROUTING_KEY);
        String message = "你好!";
        //在设置了备份交换器的情况下，mandatory不起作用
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,false, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        TimeUnit.SECONDS.sleep(3);
        System.out.println("done...");
        channel.close();
        connection.close();
    }

    private void initMQ(Channel channel) throws Exception{
        channel.exchangeDelete(EXCHANGE_NAME);
        channel.exchangeDelete("exchange_ae");
        channel.queueDelete(QUEUE_NAME);
        channel.queueDelete("queue_ae");
    }
}
