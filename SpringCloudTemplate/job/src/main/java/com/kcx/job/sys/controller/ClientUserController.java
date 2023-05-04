package com.kcx.job.sys.controller;

import com.kcx.common.utils.Result;
import com.kcx.job.sys.requestVo.ReqClientUserVO;
import com.kcx.job.sys.responseVo.ResClientUserInfoVO;
import com.kcx.job.sys.service.ClientUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ClientUserController {


    @Resource
    private ClientUserService clientUserService;

    @PostMapping(value = "job/getUserInfo")
    public Result<ResClientUserInfoVO> getUserInfo(@Validated @RequestBody ReqClientUserVO req) {
        return Result.success(clientUserService.getUserInfo(req));
    }


}
