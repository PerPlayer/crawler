package rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RabbitConsumer {

    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "47.101.189.129";
    private static final int PORT = 5672;

    public static void main(String[] args) throws Exception {
        Address[] addresses = {
                new Address(IP_ADDRESS, PORT)
        };

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection(addresses);
        Channel channel = connection.createChannel();
        channel.basicQos(64);//设置客户端最多接收未被ack的消息个数
        DefaultConsumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                System.out.println("recv message: " + new String(body));
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        channel.basicConsume(QUEUE_NAME, consumer);
        TimeUnit.SECONDS.sleep(5);
        channel.close();
        connection.close();
    }
}
