package com.kcx.service.third.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcx.common.entity.database1.ClientUserEntity;
import com.kcx.common.utils.Result;
import com.kcx.service.third.sys.dao.ClientUserDao;
import com.kcx.service.third.sys.feign.TaskProducerFeign;
import com.kcx.service.third.sys.requestVo.ReqClientUserUpdateVO;
import com.kcx.service.third.sys.service.ClientUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("clientUserService")
@Slf4j
public class ClientUserServiceImpl extends ServiceImpl<ClientUserDao, ClientUserEntity> implements ClientUserService {


    @Resource
    private TaskProducerFeign taskProducerFeign;

    @Override
    public Result<String> updateNickName(ReqClientUserUpdateVO req) {
        Result<String> res = Result.affectedRows(baseMapper.updateNickName(req));
        com.kcx.common.entityFegin.taskProducer.requestVo.ReqClientUserUpdateVO reqClientUserUpdateEntity =
                new com.kcx.common.entityFegin.taskProducer.requestVo.ReqClientUserUpdateVO();
        reqClientUserUpdateEntity.setOid("1653342883341795328");
        reqClientUserUpdateEntity.setNickName("任务提供者修改昵称");
        Result<String> result = taskProducerFeign.updateNickName(reqClientUserUpdateEntity);
        log.info("远程调用task-producer修改用户昵称结果："+result);
        return res;
    }
}
