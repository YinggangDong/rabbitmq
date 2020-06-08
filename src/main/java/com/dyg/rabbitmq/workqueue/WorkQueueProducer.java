/**
 * 版权所有 2019-2022山东新北洋信息技术股份有限公司
 * 保留所有权利。
 */
package com.dyg.rabbitmq.workqueue;

import com.dyg.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Producer类(接口)是 工作队列(work queue)的生产者
 *
 * @author dongyinggang
 * @date 2020-06-02 10:08
 **/
public class WorkQueueProducer {


    private static final String TEST_WORK_QUEUE = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        //获取一个连接
        Connection connection = ConnectionUtil.getConnection();
        //从连接中获取一个通道
        Channel channel = connection.createChannel();

        //声明队列
        channel.queueDeclare(TEST_WORK_QUEUE, false, false, false, null);

        //向队列中丢入50条数据
        for (int i = 0; i < 50; i++) {
            String msg = "Hello workQueue" + i;

            channel.basicPublish("", TEST_WORK_QUEUE, null, msg.getBytes());
            System.out.println("["+Thread.currentThread().getStackTrace()[1].getClassName()+"]  :  "+msg);
            Thread.sleep(i * 20);
        }

        channel.close();

        connection.close();
    }

}