package com.kcx.task.producer.sys.feign;

import com.kcx.common.entityFegin.taskConsumer.requestVo.ReqClientUserUpdateVO;
import com.kcx.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用task-consumer,最好都是post请求
 */
@Component
@FeignClient(value = "task-consumer")
public interface TaskConsumerFeign {

    @PostMapping(value = "taskConsumer/updateNickName")
    Result<String> updateNickName(@Validated @RequestBody ReqClientUserUpdateVO req);
}
