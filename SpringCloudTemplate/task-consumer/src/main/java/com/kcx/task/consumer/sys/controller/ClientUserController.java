package com.kcx.task.consumer.sys.controller;

import com.kcx.common.utils.Result;
import com.kcx.task.consumer.sys.requestVo.ReqClientUserUpdateVO;
import com.kcx.task.consumer.sys.service.ClientUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ClientUserController {


    @Resource
    private ClientUserService clientUserService;

    @PostMapping(value = "taskConsumer/updateNickName")
    public Result<String> updateNickName(@Validated @RequestBody ReqClientUserUpdateVO req) {
        return clientUserService.updateNickName(req);
    }
}
