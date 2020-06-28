package com.dyg.rabbitmq.multithread.pool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ProducerConsumer 类是 生产着消费者模式启动类
 *
 * @author dongyinggang
 * @date 2020-06-28 21:43
 **/
public class ProducerConsumer {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 8,
                2, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000));
        LinkedBlockingQueue queue = new LinkedBlockingQueue(3);
        ProducerThread producerThread1 = new ProducerThread(queue);
        ProducerThread producerThread2 = new ProducerThread(queue);
        ConsumerThread consumerThread1 = new ConsumerThread(queue);
        ConsumerThread consumerThread2 = new ConsumerThread(queue);
        executor.execute(producerThread1);
        executor.execute(producerThread2);
        executor.execute(consumerThread1);
        executor.execute(consumerThread2);
        //主线程休眠20s
        Thread.sleep(20000);

        //停止生产者线程
        producerThread1.stop();
        producerThread2.stop();
        while(true){
            //每5s检测一次是否还有线程存活
            Thread.sleep(5000);
            int activeCount = executor.getActiveCount();
            System.out.println("[Executor] 当前线程池活动线程数量为："+ activeCount);
            if(0 == activeCount){
                System.out.println("[Executor] 线程池关闭");
                executor.shutdown();
                break;
            }
        }
    }
}
