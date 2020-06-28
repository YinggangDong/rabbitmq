package com.dyg.rabbitmq.multithread.pool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * ProducerThread 类是 生产者线程类
 *
 * @author dongyinggang
 * @date 2020/6/28 21:16
 **/
public class ProducerThread implements Runnable {

    /**
     * 缓冲队列queue
     */
    private volatile LinkedBlockingQueue queue;

    private static boolean flag = false;

    public ProducerThread(LinkedBlockingQueue queue) {
        this.queue = queue;
    }

    public void stop(){
        flag = true;
        System.out.println("[ProducerThread] "+ Thread.currentThread().getName() + "关闭");
    }

    @Override
    public void run() {

        System.out.println("[ProducerThread] "+ Thread.currentThread().getName() + "启动");
        while (!flag) {
            try {
                //向阻塞队列中投递一条数据
                String message = "[Message]  " + Thread.currentThread().getName() + "于" +
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + "向队列投递本条信息";
                queue.put(message);
                System.out.println("[ProducerThread]  " + Thread.currentThread().getName() + "  :  send " + message);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
