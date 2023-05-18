package com.kcx.common.utils.rabbitmq;

/**
 * RabbitMQ发送消息时，消息的具体类型
 */
public enum MessageType {
    /**
     * 普通任务
     */
    normal,
    /**
     * 导出任务
     */
    export;

}
