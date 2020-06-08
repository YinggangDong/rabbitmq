/**
 * 版权所有 2019-2022山东新北洋信息技术股份有限公司
 * 保留所有权利。
 */
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
 * Consumer类(接口)是 工作队列(work queue)的消费者A
 *
 * @author dongyinggang
 * @date 2020-06-02 10:37
 **/
public class WorkQueueConsumerA {

    private static final String TEST_WORK_QUEUE = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(TEST_WORK_QUEUE, false, false, false, null);

        //定义队列的消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("["+Thread.currentThread().getStackTrace()[1].getClassName()+"]  :  "+msg);

            }
        };

        //监听队列
        channel.basicConsume(TEST_WORK_QUEUE, true, consumer);

//        channel.close();
//
//        connection.close();


    }
}
