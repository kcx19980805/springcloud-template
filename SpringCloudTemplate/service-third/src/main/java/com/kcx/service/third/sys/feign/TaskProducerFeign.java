package com.kcx.service.third.sys.feign;

import com.kcx.common.entityFegin.taskProducer.requestVo.ReqClientUserUpdateVO;
import com.kcx.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用task-producer,最好都是post请求
 */
@Component
@FeignClient(value = "task-producer")
public interface TaskProducerFeign {

    @PostMapping(value = "taskProducer/updateNickName")
    Result<String> updateNickName(@Validated @RequestBody ReqClientUserUpdateVO req);
}
