package com.kcx.service.middleware.sys.controller;

import com.kcx.common.utils.Result;
import com.kcx.service.middleware.sys.requestVo.ReqClientUserUpdateVO;
import com.kcx.service.middleware.sys.service.ClientUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class ClientUserController {


    @Resource
    private ClientUserService clientUserService;

    @PostMapping(value = "middleware/updateNickName")
    public Result<String> updateNickName(@Validated @RequestBody ReqClientUserUpdateVO req) {
        return clientUserService.updateNickName(req);
    }
}
