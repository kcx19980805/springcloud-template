package com.kcx.common.utils.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * RabbitMQ消费者基类
 */
@Slf4j
public abstract class RabbitMQBaseConsumer {
    /**
     * 缓存重试次数
     */
    private ConcurrentHashMap<String,Integer> retryCache = new ConcurrentHashMap<>();

    /**
     * 接收消息方法，子类加上@RabbitListener(queues = {"xxx"})实现消费
     * 请在这里直接调用ackMessage，并做好幂等处理
     * @param message
     * @param channel
     */
    public abstract void handleMsg(Message message, Channel channel);

    /**
     * 消费消息并手动ack
     * @param message 消息
     * @param channel 通道
     * @param handleMsg 处理消息的具体逻辑
     */
    public void ackMessage(Message message, Channel channel, Consumer<String> handleMsg){
        String exchangeName = message.getMessageProperties().getReceivedExchange();
        String queueName = message.getMessageProperties().getConsumerQueue();
        //消息标志，从1开始，每次都会递增
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //发送消息时自己传入的CorrelationData
        String correlationId =  message.getMessageProperties().getHeader("spring_returned_message_correlation");
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        Integer count = retryCache.get(correlationId);
        try {
            if(count != null){
                log.warn("重试消费RabbitMQ第{}次，消息id：{}，交换机:{}，队列:{}，消息:{}。",count,correlationId,exchangeName, queueName,msg);
            }else {
                log.info("首次消费RabbitMQ，消息id：{}，交换机:{}，队列:{}，消息:{}。",correlationId,exchangeName, queueName,msg);
            }
            handleMsg.accept(msg);
            //手动ack告诉mq消息已经正常消费
            channel.basicAck(deliveryTag, false);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                if(count == null || count < 3){
                    //出现异常的情况下,告诉mq失败，进行重发,
                    //参数1：消息的tag唯一识别消息
                    //参数2：false 多条处理
                    //参数3：false 不会重发，会把消息打入到队列绑定的死信队列 true会死循环的重发
                    retryCache.put(correlationId,count==null?1:count+1);
                    channel.basicNack(deliveryTag, false,true);
                }else{
                    log.error("消费RabbitMQ重试次数达到3次，丢弃消息，消息id：{}，交换机:{}，队列:{}，消息:{}。",count,correlationId,exchangeName, queueName,msg);
                    retryCache.remove(correlationId);
                    channel.basicAck(deliveryTag, false);
                }
            } catch (IOException e) {
                e.printStackTrace();
                handleException();
            }
        }

    }

    /**
     * 处理RabbitMQ ack失败的逻辑
     * 例如:人工干预,发短信预警,同时把消息转移别的存储DB
     */
    public void handleException(){

    }

}
