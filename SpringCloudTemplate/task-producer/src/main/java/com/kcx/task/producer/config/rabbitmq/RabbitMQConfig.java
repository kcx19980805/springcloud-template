package com.kcx.task.producer.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 交换机、队列、路由key的绑定配置
 */
@Configuration
public class RabbitMQConfig {

    /*----------------------------------------普通队列-------------------------------------------*/

    /**
     * 注册direct模式的交换机
     */
    @Bean(name = "directExchange")
    public DirectExchange directExchange(){
        return new DirectExchange("directExchange",true,false);
    }

    /**
     * 注册普通队列
     * @return
     */
    @Bean(name = "testQueue1")
    public Queue testQueue1(){
        return new Queue("testQueue1",true);
    }
    /**
     * 绑定交换机，队列，路由key
     */
    @Bean
    public Binding testQueue1Binding(@Qualifier("testQueue1") Queue testQueue1,@Qualifier("directExchange")DirectExchange fanoutExchange){
        return BindingBuilder.bind(testQueue1).to(fanoutExchange).with("testQueue1Key");
    }

    @Bean(name = "testQueue2")
    public Queue testQueue2(){
        return new Queue("testQueue2",true);
    }
    @Bean
    public Binding testQueue2Binding(@Qualifier("testQueue2")Queue testQueue2,@Qualifier("directExchange")DirectExchange fanoutExchange){
        return BindingBuilder.bind(testQueue2).to(fanoutExchange).with("testQueue2Key");
    }

    @Bean(name = "testQueue3")
    public Queue testQueue3(){
        return new Queue("testQueue3",true);
    }
    @Bean
    public Binding testQueue3Binding(@Qualifier("testQueue3")Queue testQueue3,@Qualifier("directExchange")DirectExchange fanoutExchange){
        return BindingBuilder.bind(testQueue3).to(fanoutExchange).with("testQueue3Key");
    }

    /*----------------------------------------过期队列---------------------------------------------*/
    @Bean(name = "ttlExchange")
    public DirectExchange ttlExchange(){
        return new DirectExchange("ttlExchange",true,false);
    }
    @Bean(name = "testTtlQueue")
    public Queue testTtlQueue(){
        Map<String,Object> args = new HashMap<>();
        //设置队列所有消息统一过期时间7天，过期后直接丢弃
        args.put("x-message-ttl",1000*60*60*24*7);
        return new Queue("testTtlQueue",true,false,false,args);
    }
    @Bean
    public Binding testTtlQueueBinding(@Qualifier("testTtlQueue")Queue testTtlQueue,@Qualifier("ttlExchange")DirectExchange ttlExchange){
        return BindingBuilder.bind(testTtlQueue).to(ttlExchange).with("ttlKey");
    }
    /*----------------------------------------延时队列-------------------------------------------*/

    /**
     * 定义一个延迟交换机
     * 不需要死信交换机和死信队列，支持消息延迟投递，消息投递之后没有到达投递时间，是不会投递给队列
     * 而是存储在一个分布式表，当投递时间到达，才会投递到目标队列
     * @return
     */
    @Bean("delayedExchange")
    public CustomExchange testDelayedExchange(){
        Map<String, Object> args = new HashMap<>(1);
        // 自定义交换机的类型
        args.put("x-delayed-type", "direct");
        return new CustomExchange("delayedExchange", "x-delayed-message", true, false, args);
    }
    @Bean("testDelayedQueue")
    public Queue testDelayedQueue(){
        return new Queue("testDelayedQueue");
    }
    @Bean
    public Binding testDelayedQueueBinding(@Qualifier("testDelayedQueue") Queue delayedQueue,
                                           @Qualifier("delayedExchange") CustomExchange delayedExchange){
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with("testDelayedKey").noargs();
    }
    /*----------------------------------------死信队列-------------------------------------------*/


    /**
     * 死信队列的交换机
     * @return
     */
    @Bean(name = "deadExchange")
    public DirectExchange deadExchange(){
        return new DirectExchange("deadExchange",true,false);
    }

    /**
     * 死信队列,就是一个普通队列
     */
    @Bean(name = "testDeadQueue")
    public Queue testDeadQueue(){
        return new Queue("testDeadQueue",true);
    }
    @Bean
    public Binding testDeadQueueBinding(@Qualifier("deadExchange")DirectExchange deadExchange,@Qualifier("testDeadQueue") Queue testDeadQueue){
        return BindingBuilder.bind(testDeadQueue).to(deadExchange).with("testDeadKey");
    }


    /**
     * 绑定死信队列的普通队列
     */
    @Bean(name = "testTtlDeadQueue")
    public Queue testTtlDeadQueue(){
        //设置队列过期时间
        Map<String,Object> args = new HashMap<>();
        //设置队列所有消息统一过期时间7天，过期的转入死信队列
        args.put("x-message-ttl",1000*60*60*24*7);
        //消息长度，最多存储10万条，多余的转入死信队列
        args.put("x-max-length",100000);
        //设置死信队列
        args.put("x-dead-letter-exchange","deadExchange");
        args.put("x-dead-letter-routing-key","testDeadKey");
        return new Queue("testTtlDeadQueue",true,false,false,args);
    }
    @Bean
    public Binding testTtlDeadQueueBinding(@Qualifier("deadExchange")DirectExchange deadExchange,@Qualifier("testTtlDeadQueue") Queue testTtlDeadQueue){
        return BindingBuilder.bind(testTtlDeadQueue).to(deadExchange).with("testTtlDeadKey");
    }

    /**
     * 绑定死信队列的普通队列2
     */
    @Bean(name = "testTtlDeadQueue2")
    public Queue testTtlDeadQueue2(){
        //设置队列过期时间
        Map<String,Object> args = new HashMap<>();
        args.put("x-message-ttl",1000*5);
        args.put("x-max-length",10);
        //设置死信队列
        args.put("x-dead-letter-exchange","deadExchange");
        args.put("x-dead-letter-routing-key","testDeadKey");
        return new Queue("testTtlDeadQueue2",true,false,false,args);
    }

    @Bean
    public Binding testTtlDeadQueue2Binding(@Qualifier("deadExchange")DirectExchange deadExchange,@Qualifier("testTtlDeadQueue2") Queue testTtlDeadQueue2){
        return BindingBuilder.bind(testTtlDeadQueue2).to(deadExchange).with("testTtlDeadKey2");
    }
}
