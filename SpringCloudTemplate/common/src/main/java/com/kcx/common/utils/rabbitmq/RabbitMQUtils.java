package com.kcx.common.utils.rabbitmq;

import com.kcx.common.utils.id.SnowFlakeIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * RabbitMQ工具类
 * 主要是分第一种快速发送，如果网络不好可能会丢失
 * 第二中是失败重试，确保一定发送成功，否则返回false
 */
@Slf4j
public class RabbitMQUtils {

    /**
     * 普通发送消息，内存少，发送快，但不确保一定发送成功
     *
     * @param exchangeName   交换机
     * @param routingKey     路由key
     * @param message        消息
     * @param rabbitTemplate ioc容器中的rabbitTemplate
     */
    public static void sendMsg(String exchangeName, String routingKey, String message, RabbitTemplate rabbitTemplate) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message, new CorrelationData(String.valueOf(SnowFlakeIdUtils.getSnowflakeId())));
    }

    /**
     * 严格发送消息，确保服务器一定收到，失败重试三次
     *
     * @param exchangeName   交换机
     * @param routingKey     路由key
     * @param message        消息
     * @param rabbitTemplate ioc容器中的rabbitTemplate
     * @return 发送成功true/失败false
     */
    public static boolean sendConfirmMsg(String exchangeName, String routingKey, String message, RabbitTemplate rabbitTemplate) {
        return confirmMsg(exchangeName, routingKey, message, rabbitTemplate, rabbitTemplate1 -> rabbitTemplate1.convertAndSend(exchangeName, routingKey,
                message, new CorrelationData(String.valueOf(SnowFlakeIdUtils.getSnowflakeId()))));
    }

    /**
     * 发送延迟消息，内存少，发送快，但不确保一定发送成功
     *
     * @param exchangeName   延迟交换机
     * @param routingKey     路由key
     * @param message        消息
     * @param second         延迟时间，单位秒
     * @param rabbitTemplate ioc容器中的rabbitTemplate
     */
    public static void sendDelayedMsg(String exchangeName, String routingKey, String message, int second, RabbitTemplate rabbitTemplate) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message, message1 -> {
            //延迟10秒
            message1.getMessageProperties().setDelay(second * 1000);
            return message1;
        }, new CorrelationData(String.valueOf(SnowFlakeIdUtils.getSnowflakeId())));
    }

    /**
     * 严格发送延迟消息，确保服务器一定收到，失败重试三次
     *
     * @param exchangeName   延迟交换机
     * @param routingKey     路由key
     * @param message        消息
     * @param second         延迟时间，单位秒
     * @param rabbitTemplate ioc容器中的rabbitTemplate
     * @return 发送成功true/失败false
     */
    public static boolean sendDelayedConfirmMsg(String exchangeName, String routingKey, String message, int second, RabbitTemplate rabbitTemplate) {
        return confirmMsg(exchangeName, routingKey, message, rabbitTemplate, rabbitTemplate1 -> rabbitTemplate1.convertAndSend(exchangeName, routingKey, message, message1 -> {
            //延迟时间
            message1.getMessageProperties().setDelay(second * 1000);
            return message1;
        }, new CorrelationData(String.valueOf(SnowFlakeIdUtils.getSnowflakeId()))));
    }

    /**
     * 发送带过期时间的消息，过期未消费后自动删除或者如果有死信队列则丢进死信队列，内存少，发送快，但不确保一定发送成功
     *
     * @param exchangeName   交换机
     * @param routingKey     路由key
     * @param message        消息
     * @param second         过期时间，单位秒
     * @param rabbitTemplate ioc容器中的rabbitTemplate
     */
    public static void sendTTLMsg(String exchangeName, String routingKey, String message, int second, RabbitTemplate rabbitTemplate) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message, message1 -> {
            //设置消息的过期时间
            message1.getMessageProperties().setExpiration(String.valueOf(second * 1000));
            message1.getMessageProperties().setContentEncoding("UTF-8");
            return message1;
        }, new CorrelationData(String.valueOf(SnowFlakeIdUtils.getSnowflakeId())));
    }

    /**
     * 严格发送带过期时间的消息，过期未消费后自动删除或者如果有死信队列则丢进死信队列，确保服务器一定收到，失败重试三次
     *
     * @param exchangeName   交换机
     * @param routingKey     路由key
     * @param message        消息
     * @param second         过期时间，单位秒
     * @param rabbitTemplate ioc容器中的rabbitTemplate
     */
    public static boolean sendTTLConfirmMsg(String exchangeName, String routingKey, String message, int second, RabbitTemplate rabbitTemplate) {
        return confirmMsg(exchangeName, routingKey, message, rabbitTemplate, rabbitTemplate1 -> rabbitTemplate1.convertAndSend(exchangeName, routingKey, message, message1 -> {
            //设置消息的过期时间
            message1.getMessageProperties().setExpiration(String.valueOf(second * 1000));
            message1.getMessageProperties().setContentEncoding("UTF-8");
            return message1;
        }, new CorrelationData(String.valueOf(SnowFlakeIdUtils.getSnowflakeId()))));
    }

    /**
     * 确认消息机制，在失败后重试三次
     *
     * @param exchangeName   交换机
     * @param routingKey     路由key
     * @param message        消息
     * @param rabbitTemplate ioc容器中的rabbitTemplate
     * @param consumer       发送消息的具体代码
     * @return
     */
    public static boolean confirmMsg(String exchangeName, String routingKey, String message, RabbitTemplate rabbitTemplate, Consumer<RabbitTemplate> consumer) {
        //这里声明数组才能存在堆中，否则存在栈上，回调线程拿不到
        Boolean[] b = {false};
        for (int i = 0; i < 4; i++) {
            //重新创建一个对象，避免多线程下，一个rabbitTemplate被设置多个ConfirmCallback
            rabbitTemplate = new RabbitTemplate(rabbitTemplate.getConnectionFactory());
            CountDownLatch latch = new CountDownLatch(1);
            rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
                if (ack) {
                    //如果MQ服务器成功接收
                    b[0] = true;
                }
                latch.countDown();
            });
            if (i > 0) {
                log.warn("RabbitMQ投递消息失败，交换机{}，队列{}，消息{}，开始第{}次重试", exchangeName, routingKey, message, i);
            }
            consumer.accept(rabbitTemplate);
            try {
                //等待MQ服务器回调
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            if (b[0]) {
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return b[0];
    }


}
