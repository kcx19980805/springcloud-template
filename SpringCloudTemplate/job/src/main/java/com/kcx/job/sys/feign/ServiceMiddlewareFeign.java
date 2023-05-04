package com.kcx.job.sys.feign;

import com.kcx.common.entityFegin.serviceMiddleware.requestVo.ReqClientUserUpdateVO;
import com.kcx.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用service-middleware,最好都是post请求
 */
@Component
@FeignClient(value = "service-middleware")
public interface ServiceMiddlewareFeign {

    @PostMapping(value = "/middleware/updateNickName")
    Result<String> updateNickName(@Validated @RequestBody ReqClientUserUpdateVO req);
}
