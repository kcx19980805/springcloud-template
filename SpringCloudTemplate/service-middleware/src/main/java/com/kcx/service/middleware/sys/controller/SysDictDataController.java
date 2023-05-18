package com.kcx.service.middleware.sys.controller;


import com.kcx.common.entityFegin.apiUser.requestVo.ReqSysDictDataListVO;
import com.kcx.common.entityFegin.apiUser.responseVo.ResSysDictDataListVO;
import com.kcx.common.utils.Result;
import com.kcx.common.utils.page.responseVo.ResPageDataVO;
import com.kcx.service.middleware.sys.service.SysDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "系统字典库相关")
public class SysDictDataController {

    @Resource
    private SysDictDataService sysDictDataService;

    @PostMapping ("dict/list")
    @ApiOperation(value = "系统字典库列表")
    public Result<ResPageDataVO<ResSysDictDataListVO>> list(@RequestBody @Validated ReqSysDictDataListVO req) {
        return Result.success(sysDictDataService.list(req));
    }
}
