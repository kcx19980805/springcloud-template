package com.kcx.api.user.sys.feign;

import com.kcx.common.entityFegin.job.requestVo.ReqClientUserVO;
import com.kcx.common.entityFegin.job.responseVo.ResClientUserInfoVO;
import com.kcx.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用job,最好都是post请求
 */
@Component
@FeignClient(value = "job")
public interface JobFeign {

    @PostMapping(value = "job/getUserInfo")
    Result<ResClientUserInfoVO> getUserInfo(@Validated @RequestBody ReqClientUserVO req);

}
