package com.kcx.task.producer.sys.controller;

import com.kcx.common.utils.Result;
import com.kcx.task.producer.sys.requestVo.ReqClientUserUpdateVO;
import com.kcx.task.producer.sys.service.ClientUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ClientUserController {


    @Resource
    private ClientUserService clientUserService;

    @PostMapping(value = "taskProducer/updateNickName")
    public Result<String> updateNickName(@Validated @RequestBody ReqClientUserUpdateVO req) {
        return clientUserService.updateNickName(req);
    }
}
