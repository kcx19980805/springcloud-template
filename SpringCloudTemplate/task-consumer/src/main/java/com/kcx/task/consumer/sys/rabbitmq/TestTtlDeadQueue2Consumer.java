package com.kcx.task.consumer.sys.rabbitmq;

import com.kcx.common.utils.rabbitmq.RabbitMQBaseConsumer;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * 死信队列未过期死亡的信息消费者模块2
 */
@Service
public class TestTtlDeadQueue2Consumer extends RabbitMQBaseConsumer{


    @Override
    @RabbitListener(queues = {"testTtlDeadQueue2"})
    public void handleMsg(Message message, Channel channel) {
        ackMessage(message, channel, s -> {
            System.out.println("testTtlDeadQueue2接收到未过期死亡的消息："+s);
        });
    }
}
