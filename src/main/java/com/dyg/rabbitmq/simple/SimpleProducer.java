package com.dyg.rabbitmq.simple;

import com.dyg.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * SimpleProducer类(接口)是 简单队列的生产者
 *
 * @author dongyinggang
 * @date 2020-06-02 10:08
 **/
public class SimpleProducer {


    private static final String TEST_SIMPLE_QUEUE = "test_simple_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        //获取一个连接
        Connection connection = ConnectionUtil.getConnection();
        //从连接中获取一个通道
        Channel channel = connection.createChannel();

        //声明队列
        channel.queueDeclare(TEST_SIMPLE_QUEUE,false,false,false,null);

        String msg = "Hello Simple !";

        channel.basicPublish("",TEST_SIMPLE_QUEUE,null,msg.getBytes());

        System.out.println("--send msg:" + msg);

        channel.close();

        connection.close();
    }

}
