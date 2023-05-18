package com.kcx.task.producer.sys.rabbitmq;

import com.kcx.common.utils.rabbitmq.RabbitMQUtils;
import com.kcx.task.producer.sys.rabbitmq.RabbitMQProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 所有消息队列生产者模块
 */
@Service("rabbitMQService")
public class RabbitMQProducer{

    @Resource
    private RabbitTemplate rabbitTemplate;
    /**
     * 发送普通消息到testQueue1队列
     */
    public boolean sendToTestQueue1(String msg) {
        return RabbitMQUtils.sendConfirmMsg("directExchange","testQueue1Key",msg,rabbitTemplate);
    }
    /**
     * 发送普通消息到testQueue2队列
     */
    public boolean sendToTestQueue2(String msg) {
        return RabbitMQUtils.sendConfirmMsg("directExchange","testQueue2Key",msg,rabbitTemplate);
    }
    /**
     * 发送普通消息到testQueue3队列
     */
    public boolean sendToTestQueue3(String msg) {
        return RabbitMQUtils.sendConfirmMsg("directExchange","testQueue3Key",msg,rabbitTemplate);
    }
    /**
     * 发送过期删除消息到testTtlQueue队列
     */
    public boolean sendToTestTtlQueue(String msg) {
        return RabbitMQUtils.sendTTLConfirmMsg("ttlExchange","ttlKey",msg,20,rabbitTemplate);
    }
    /**
     * 发送延时消息到testDelayedQueue队列
     */
    public boolean sendToTestDelayedQueue(String msg) {
        return RabbitMQUtils.sendDelayedConfirmMsg("delayedExchange","testDelayedKey",msg,10,rabbitTemplate);
    }
    /**
     * 发送死信消息到testDeadQueue队列
     */
    public boolean sendToTestDeadQueue(String msg) {
        return RabbitMQUtils.sendConfirmMsg("deadExchange","testTtlDeadKey",msg,rabbitTemplate);
    }

    /**
     * 发送死信消息到testDeadQueue2队列
     */
    public boolean sendToTestDeadQueue2(String msg) {
        return RabbitMQUtils.sendConfirmMsg("deadExchange","testTtlDeadKey2",msg,rabbitTemplate);
    }
}
