package com.kcx.task.producer.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.kcx.common.utils.Result;
import com.kcx.common.utils.rabbitmq.MessageType;
import com.kcx.common.utils.rabbitmq.RabbitMQMessageVo;
import com.kcx.task.producer.sys.requestVo.ReqRabbitMQVO;
import com.kcx.task.producer.sys.rabbitmq.RabbitMQProducer;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RabbitMQController {

    @Resource
    private RabbitMQProducer rabbitMQService;

    /*-----------------------------------普通消息-------------------------------------*/
    @PostMapping(value = "sendToTestQueue1")
    public Result<String> sendToTestQueue1(@Validated @RequestBody ReqRabbitMQVO req) {
        RabbitMQMessageVo rabbitMQMessageVo = new RabbitMQMessageVo();
        rabbitMQMessageVo.setMsg(req.getMsg());
        rabbitMQMessageVo.setType(MessageType.normal);
        if(rabbitMQService.sendToTestQueue1(JSONObject.toJSONString(rabbitMQMessageVo))){
            return Result.success("消息发送成功");
        }
        return Result.error("消息发送失败");
    }

    @PostMapping(value = "sendToTestQueue2")
    public Result<String> sendToTestQueue2(@Validated @RequestBody ReqRabbitMQVO req) {
        RabbitMQMessageVo rabbitMQMessageVo = new RabbitMQMessageVo();
        rabbitMQMessageVo.setMsg(req.getMsg());
        rabbitMQMessageVo.setType(MessageType.normal);
        if(rabbitMQService.sendToTestQueue2(JSONObject.toJSONString(rabbitMQMessageVo))){
            return Result.success("消息发送成功");
        }
        return Result.error("消息发送失败");
    }

    @PostMapping(value = "sendToTestQueue3")
    public Result<String> sendToTestQueue3(@Validated @RequestBody ReqRabbitMQVO req) {
        RabbitMQMessageVo rabbitMQMessageVo = new RabbitMQMessageVo();
        rabbitMQMessageVo.setMsg(req.getMsg());
        rabbitMQMessageVo.setType(MessageType.normal);
        if(rabbitMQService.sendToTestQueue3(JSONObject.toJSONString(rabbitMQMessageVo))){
            return Result.success("消息发送成功");
        }
        return Result.error("消息发送失败");
    }
    /*-----------------------------------特殊消息-------------------------------------*/

    @PostMapping(value = "sendToTestTtlQueue")
    public Result<String> sendToTestTtlQueue(@Validated @RequestBody ReqRabbitMQVO req) {
        RabbitMQMessageVo rabbitMQMessageVo = new RabbitMQMessageVo();
        rabbitMQMessageVo.setMsg(req.getMsg());
        rabbitMQMessageVo.setType(MessageType.normal);
        if(rabbitMQService.sendToTestTtlQueue(JSONObject.toJSONString(rabbitMQMessageVo))){
            return Result.success("过期删除消息发送成功");
        }
        return Result.error("过期删除消息消息发送失败");
    }

    @PostMapping(value = "sendToTestDelayedQueue")
    public Result<String> sendToTestDelayedQueue(@Validated @RequestBody ReqRabbitMQVO req) {
        RabbitMQMessageVo rabbitMQMessageVo = new RabbitMQMessageVo();
        rabbitMQMessageVo.setMsg(req.getMsg());
        rabbitMQMessageVo.setType(MessageType.normal);
        if(rabbitMQService.sendToTestDelayedQueue(JSONObject.toJSONString(rabbitMQMessageVo))){
            return Result.success("延时消息发送成功");
        }
        return Result.error("延时消息发送失败");
    }

    @PostMapping(value = "sendToTestDeadQueue")
    public Result<String> sendToTestDeadQueue(@Validated @RequestBody ReqRabbitMQVO req) {
        RabbitMQMessageVo rabbitMQMessageVo = new RabbitMQMessageVo();
        rabbitMQMessageVo.setMsg(req.getMsg());
        rabbitMQMessageVo.setType(MessageType.normal);
        if(rabbitMQService.sendToTestDeadQueue(JSONObject.toJSONString(rabbitMQMessageVo))){
            return Result.success("死信消息发送成功");
        }
        return Result.error("死信消息发送失败");
    }

    @PostMapping(value = "sendToTestDeadQueue2")
    public Result<String> sendToTestDeadQueue2(@Validated @RequestBody ReqRabbitMQVO req) {
        RabbitMQMessageVo rabbitMQMessageVo = new RabbitMQMessageVo();
        rabbitMQMessageVo.setMsg(req.getMsg());
        rabbitMQMessageVo.setType(MessageType.normal);
        if(rabbitMQService.sendToTestDeadQueue2(JSONObject.toJSONString(rabbitMQMessageVo))){
            return Result.success("死信消息发送成功");
        }
        return Result.error("死信消息发送失败");
    }

}
