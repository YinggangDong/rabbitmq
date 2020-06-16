package com.dyg.rabbitmq.routing;

import com.dyg.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RoutingProducer {

    private static final String EXCHANGE_NAME = "test_exchange_direct";

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
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String msg = "Hello direct !";

        //设置路由键，分别发送路由键为error和info的两条信息
        String routingKey = "error";
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());

        System.out.println("[RoutingProducer] send:" + " [" + routingKey + "] " + msg);

        String routingKey1 = "info";
        channel.basicPublish(EXCHANGE_NAME, routingKey1, null, msg.getBytes());

        System.out.println("[RoutingProducer] send:" + " [" + routingKey1 + "] " + msg);

        channel.close();

        connection.close();
    }
}
