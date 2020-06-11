package com.dyg.rabbitmq.workqueue;

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
 * WorkQueueConsumerB类(接口)是 工作队列(work queue)的消费者B
 *
 * @author dongyinggang
 * @date 2020-06-03 09:02
 **/
public class WorkQueueConsumerB {

    private static final String TEST_WORK_QUEUE = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //获取channel
        Channel channel = connection.createChannel();

        channel.queueDeclare(TEST_WORK_QUEUE,false,false,false,null);

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("[Consumer B]  Recv  :  " + msg);
                //每过1s消费一条消息
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("[Consumer B]  down");
                }
            }
        };

        boolean autoAck = true;
        //监听队列
        channel.basicConsume(TEST_WORK_QUEUE, autoAck, consumer);


    }
}
