package com.kcx.api.admin.sys.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.kcx.api.admin.sys.requestVo.*;
import com.kcx.api.admin.sys.responseVo.ResUserListVO;
import com.kcx.api.admin.sys.responseVo.ResUserLoginVO;
import com.kcx.api.admin.sys.service.SysUserService;
import com.kcx.common.utils.Result;
import com.kcx.common.utils.page.ResPageDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 账号管理
 */
@RestController
@Api(tags = "账号管理")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @PostMapping("user/login")
    @ApiOperation(value = "管理员登录")
    @SentinelResource
    public Result userLogin(@RequestBody @Validated ReqUserLoginVO req) {
        return Result.success(sysUserService.userLogin(req));
    }

    @GetMapping("user/list")
    @ApiOperation(value = "管理员列表")
    public Result<ResPageDataVO<ResUserListVO>> userList(@Validated ReqUserListVO requestUserListEntity) {
        return Result.success(sysUserService.userList(requestUserListEntity));
    }

}
