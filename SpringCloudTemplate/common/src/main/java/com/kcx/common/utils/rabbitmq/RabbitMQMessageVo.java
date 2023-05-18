package com.kcx.common.utils.rabbitmq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * mq消息传输对象
 */
@Data
public class RabbitMQMessageVo {
    @NotBlank(message = "消息类型不能为空")
    @ApiModelProperty(value = "消息类型",required = true)
    private MessageType type;

    @NotBlank(message = "消息不能为空")
    @ApiModelProperty(value = "消息",required = true)
    private String msg;
}
