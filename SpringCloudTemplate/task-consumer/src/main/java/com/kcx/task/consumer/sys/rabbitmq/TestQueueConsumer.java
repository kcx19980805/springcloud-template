package com.kcx.task.consumer.sys.rabbitmq;

import com.kcx.common.utils.rabbitmq.RabbitMQBaseConsumer;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * 普通队列消费者模块
 */
@Service
public class TestQueueConsumer extends RabbitMQBaseConsumer{


    @Override
    /**
     * 同时消费三个队列
     */
    @RabbitListener(queues = {"testQueue1","testQueue2","testQueue3"})
    public void handleMsg(Message message, Channel channel) {
        ackMessage(message, channel, s -> {
            System.out.println("接收到普通队列消息："+s);
        });
    }
}
