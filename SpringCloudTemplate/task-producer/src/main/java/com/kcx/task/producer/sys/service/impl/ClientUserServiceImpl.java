package com.kcx.task.producer.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcx.common.entity.database1.ClientUserEntity;
import com.kcx.common.utils.Result;
import com.kcx.task.producer.sys.dao.ClientUserDao;
import com.kcx.task.producer.sys.feign.TaskConsumerFeign;
import com.kcx.task.producer.sys.requestVo.ReqClientUserUpdateVO;
import com.kcx.task.producer.sys.service.ClientUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("clientUserService")
@Slf4j
public class ClientUserServiceImpl extends ServiceImpl<ClientUserDao, ClientUserEntity> implements ClientUserService {
    @Resource
    private TaskConsumerFeign taskConsumerFeign;

    @Override
    public Result<String> updateNickName(ReqClientUserUpdateVO req) {
        Result<String> res = Result.affectedRows(baseMapper.updateNickName(req));
        com.kcx.common.entityFegin.taskConsumer.requestVo.ReqClientUserUpdateVO reqClientUserUpdateEntity =
                new com.kcx.common.entityFegin.taskConsumer.requestVo.ReqClientUserUpdateVO();
        reqClientUserUpdateEntity.setOid("1653342883341795328");
        reqClientUserUpdateEntity.setNickName("任务消费者修改昵称");
        Result<String> result = taskConsumerFeign.updateNickName(reqClientUserUpdateEntity);
        log.info("远程调用task-consumer修改用户昵称结果："+result);
        return res;
    }
}
