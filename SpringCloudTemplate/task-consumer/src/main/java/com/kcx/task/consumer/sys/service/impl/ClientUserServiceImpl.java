package com.kcx.task.consumer.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcx.common.entity.database1.ClientUserEntity;
import com.kcx.common.utils.Result;
import com.kcx.task.consumer.sys.dao.ClientUserDao;
import com.kcx.task.consumer.sys.requestVo.ReqClientUserUpdateVO;
import com.kcx.task.consumer.sys.service.ClientUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service("clientUserService")
@Slf4j
public class ClientUserServiceImpl extends ServiceImpl<ClientUserDao, ClientUserEntity> implements ClientUserService {


    @Override
    public Result<String> updateNickName(ReqClientUserUpdateVO req) {
        return Result.affectedRows(baseMapper.updateNickName(req));
    }
}
