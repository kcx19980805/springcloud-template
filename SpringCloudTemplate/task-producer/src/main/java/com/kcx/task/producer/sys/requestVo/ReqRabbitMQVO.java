package com.kcx.task.producer.sys.requestVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * MQ消息
 */
@Data
public class ReqRabbitMQVO {
    @NotBlank(message = "消息不能为空")
    @ApiModelProperty(value = "消息",required = true)
    private String msg;
}
