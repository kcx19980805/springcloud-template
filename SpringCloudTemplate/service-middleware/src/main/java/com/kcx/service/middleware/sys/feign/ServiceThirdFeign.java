package com.kcx.service.middleware.sys.feign;

import com.kcx.common.entityFegin.serviceThird.requestVo.ReqClientUserUpdateVO;
import com.kcx.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用service-third,最好都是post请求
 */
@Component
@FeignClient(value = "service-third")
public interface ServiceThirdFeign {

    @PostMapping(value = "third/updateNickName")
    Result<String> updateNickName(@Validated @RequestBody ReqClientUserUpdateVO req);
}
