package com.kcx.service.middleware.sys.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcx.common.entity.database1.ClientUserEntity;
import com.kcx.common.utils.Result;
import com.kcx.service.middleware.sys.dao.ClientUserDao;
import com.kcx.service.middleware.sys.feign.ServiceThirdFeign;
import com.kcx.service.middleware.sys.requestVo.ReqClientUserUpdateVO;
import com.kcx.service.middleware.sys.service.ClientUserService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("clientUserService")
@Slf4j
public class ClientUserServiceImpl extends ServiceImpl<ClientUserDao, ClientUserEntity> implements ClientUserService {

    @Resource
    private ServiceThirdFeign serviceThirdFeign;

    @Override
    @DS("slave1")
    public Result<String> updateNickName(ReqClientUserUpdateVO req) {
        Result<String> res = Result.affectedRows(baseMapper.updateNickName(req));
        com.kcx.common.entityFegin.serviceThird.requestVo.ReqClientUserUpdateVO reqClientUserUpdateEntity =
                new com.kcx.common.entityFegin.serviceThird.requestVo.ReqClientUserUpdateVO();
        reqClientUserUpdateEntity.setOid("1653342883341795328");
        reqClientUserUpdateEntity.setNickName("第三方修改昵称");
        Result<String> result = serviceThirdFeign.updateNickName(reqClientUserUpdateEntity);
        log.info("远程调用service-third修改用户昵称结果："+result);
        return res;
    }
}
