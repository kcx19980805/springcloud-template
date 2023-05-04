package com.kcx.api.user.sys.controller;

import com.kcx.api.user.sys.requestVo.*;
import com.kcx.api.user.sys.responseVo.ResSendSmsVO;
import com.kcx.api.user.sys.service.ClientUserService;
import com.kcx.common.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "用户登录相关")
public class ClientUserController {

    @Resource
    private ClientUserService clientUserService;

    @PostMapping(value = "user/sendSms")
    @ApiOperation(value = "发送短信")
    public Result<ResSendSmsVO> sendSms(@RequestBody @Validated ReqSendSmsVO req) {
        return Result.success(clientUserService.sendSms(req));
    }

    @PostMapping(value = "user/register")
    @ApiOperation(value = "注册")
    public Result<String> register(@RequestBody @Validated ReqRegisterVO req) {
        return clientUserService.register(req);
    }

    @PostMapping(value = "user/login")
    @ApiOperation(value = "登录")
    public Result login(@RequestBody @Validated ReqLoginVO req) {
        return Result.success(clientUserService.login(req));
    }


}
