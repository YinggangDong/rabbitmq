/**
 * 版权所有 2019-2022山东新北洋信息技术股份有限公司
 * 保留所有权利。
 */
package com.dyg.rabbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ConnectionUtil类(接口)是 RabbitMQ连接工具类
 *
 * @author dongyinggang
 * @date 2020-06-02 10:02
 **/
public class ConnectionUtil {

    /**
     * getConnection方法是 获取MQ连接
     *
     * @param
     * @return
     * @author dongyinggang
     * @date 2020/6/2 10:03
     */
    public static Connection getConnection() throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("127.0.0.1");

        connectionFactory.setPort(5672);

        connectionFactory.setVirtualHost("/test_dyg");

        connectionFactory.setUsername("user_dyg");

        connectionFactory.setPassword("123");

        return connectionFactory.newConnection();

    }
}
