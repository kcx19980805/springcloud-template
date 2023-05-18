package com.kcx.api.user.sys.controller;

import com.kcx.api.user.sys.requestVo.ReqSysDictDataListVO;
import com.kcx.api.user.sys.responseVo.ResSysDictDataListVO;
import com.kcx.api.user.sys.service.SysDictDataService;
import com.kcx.common.utils.Result;
import com.kcx.common.utils.page.responseVo.ResPageDataVO;
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
