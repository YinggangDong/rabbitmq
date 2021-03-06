package com.dyg.rabbitmq.ps;

import com.dyg.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Publisher {

    private static final String EXCHANGE_NAME = "test_exchange_fanout";

    public static void main(String[] args) throws IOException, TimeoutException {

        //获取一个连接
        Connection connection = ConnectionUtil.getConnection();
        //从连接中获取一个通道
        Channel channel = connection.createChannel();

        /*
            声明交换机
            类型：
            1.匿名交换机:""
            2.fanout(不处理路由键):"fanout" 和他绑定的队列都可以收到消息
            3.Direct(处理路由键):"direct" 根据routingkey路由到对应队列
         */
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        String msg = "Hello ps !";

        //如果无绑定队列，则消息丢失，因为在mq中只有队列有存储消息的能力，交换机并不能存储消息
        channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes());

        System.out.println("[RoutingProducer] send:" + msg);

        channel.close();

        connection.close();
    }
}
