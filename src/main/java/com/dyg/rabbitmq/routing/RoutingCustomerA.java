package com.dyg.rabbitmq.routing;

import com.dyg.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RoutingCustomerA {

    private static final String QUEUE_NAME = "test_queue_direct_1";
    private static final String EXCHANGE_NAME = "test_exchange_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //将队列绑定到交换机上,并且指定路由键,可以多次绑定多个routing key，此处绑定一个routing key
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "error");

        //保证一次只分发一个
        channel.basicQos(1);

        //定义队列的消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("[Routing Customer A]  Recv  :  " + " [" + envelope.getRoutingKey() + "] " + msg);
                //每过2s消费一条消息
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("[Routing Customer A]  down");
                    //手动确认
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        //设置自动确认
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
