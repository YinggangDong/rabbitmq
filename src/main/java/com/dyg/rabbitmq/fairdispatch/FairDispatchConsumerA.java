package com.dyg.rabbitmq.fairdispatch;

import com.dyg.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * FairDispatchConsumerA类(接口)是 工作队列(work queue)-公平分发-消费者A
 *
 * @author dongyinggang
 * @date 2020-06-02 10:37
 **/
public class FairDispatchConsumerA {

    private static final String TEST_FAIR_DISPATCH_QUEUE = "test_fair_dispatch_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取链接
        Connection connection = ConnectionUtil.getConnection();
        //获取channel
        Channel channel = connection.createChannel();

        //设置持久化配置
        boolean durable = true;
        //声明队列
        channel.queueDeclare(TEST_FAIR_DISPATCH_QUEUE, durable, false, false, null);

        //保证一次只分发一个
        channel.basicQos(1);

        //定义队列的消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("[FairDispatch Consumer A]  Recv  :  " + msg);
                //每过2s消费一条消息
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("[FairDispatch Consumer A]  down");
                    //手动确认
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        //设置自动确认
        boolean autoAck = false;
        channel.basicConsume(TEST_FAIR_DISPATCH_QUEUE, autoAck, consumer);


    }
}
