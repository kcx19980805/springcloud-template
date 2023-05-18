package com.kcx.task.consumer.sys.rabbitmq;

import com.kcx.common.utils.rabbitmq.RabbitMQBaseConsumer;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * 过期未消费会自动删除的消费者模块
 */
@Service
public class TestTtlQueueConsumer  extends RabbitMQBaseConsumer {
    @Override
    @RabbitListener(queues = {"testTtlQueue"})
    public void handleMsg(Message message, Channel channel) {
        ackMessage(message, channel, s -> {
            System.out.println("testTtlQueue接收到过期删除的消息："+s);
        });
    }
}
