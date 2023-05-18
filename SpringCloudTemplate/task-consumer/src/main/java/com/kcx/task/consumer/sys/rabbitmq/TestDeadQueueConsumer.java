package com.kcx.task.consumer.sys.rabbitmq;

import com.kcx.common.utils.rabbitmq.RabbitMQBaseConsumer;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * 死信队列已经过期死亡的信息消费者模块
 */
@Service
public class TestDeadQueueConsumer  extends RabbitMQBaseConsumer {

    @Override
    @RabbitListener(queues = {"testDeadQueue"})
    public void handleMsg(Message message, Channel channel) {
        ackMessage(message, channel, s -> {
            System.out.println("接收到已经过期死亡消息："+s);
        });
    }

}
