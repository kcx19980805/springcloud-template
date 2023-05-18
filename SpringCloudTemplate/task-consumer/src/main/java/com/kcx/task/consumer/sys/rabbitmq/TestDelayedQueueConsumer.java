package com.kcx.task.consumer.sys.rabbitmq;

import com.kcx.common.utils.rabbitmq.RabbitMQBaseConsumer;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * 延迟队列消费者模块
 */
@Service
public class TestDelayedQueueConsumer extends RabbitMQBaseConsumer{


    @Override
    @RabbitListener(queues = {"testDelayedQueue"})
    public void handleMsg(Message message, Channel channel) {
        ackMessage(message, channel, s -> {
            System.out.println("接收到延迟队列消息："+s);
        });
    }
}
