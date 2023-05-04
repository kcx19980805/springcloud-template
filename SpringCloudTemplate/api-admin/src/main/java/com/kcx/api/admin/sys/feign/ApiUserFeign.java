package com.kcx.api.admin.sys.feign;

import com.kcx.common.entityFegin.apiUser.requestVo.ReqSysDictDataListVO;
import com.kcx.common.entityFegin.apiUser.responseVo.ResSysDictDataListVO;
import com.kcx.common.utils.Result;
import com.kcx.common.utils.page.ResPageDataVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用api-user,最好都是post请求
 */
@Component
@FeignClient(value = "api-user")
public interface ApiUserFeign {

    /**
     * 系统字典库列表
     * @param req
     */
    @PostMapping(value = "dict/list")
    Result<ResPageDataVO<ResSysDictDataListVO>> list(@RequestBody ReqSysDictDataListVO req);
}
