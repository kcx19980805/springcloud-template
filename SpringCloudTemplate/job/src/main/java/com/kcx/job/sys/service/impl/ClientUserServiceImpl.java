package com.kcx.job.sys.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcx.common.entity.database1.ClientUserEntity;
import com.kcx.common.entityFegin.serviceMiddleware.requestVo.ReqClientUserUpdateVO;
import com.kcx.common.utils.Result;
import com.kcx.job.sys.feign.ServiceMiddlewareFeign;
import com.kcx.job.sys.requestVo.ReqClientUserVO;
import com.kcx.job.sys.responseVo.ResClientUserInfoVO;
import com.kcx.job.sys.dao.ClientUserDao;
import com.kcx.job.sys.service.ClientUserService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author kcx
 * @description:
 * @date 2023/4/20 18:27
 */
@Service("clientUserService")
@Slf4j
public class ClientUserServiceImpl extends ServiceImpl<ClientUserDao, ClientUserEntity> implements ClientUserService {

    @Resource
    private ServiceMiddlewareFeign serviceMiddlewareFeign;


    @Override
    public ResClientUserInfoVO getUserInfo(ReqClientUserVO req) {
        ResClientUserInfoVO resClientUserInfoVO = baseMapper.userInfo(req.getUserId());
        ReqClientUserUpdateVO reqClientUserUpdateEntity = new ReqClientUserUpdateVO();
        reqClientUserUpdateEntity.setOid("1653342883341795328");
        reqClientUserUpdateEntity.setNickName("中间件修改昵称");
        Result<String> result = serviceMiddlewareFeign.updateNickName(reqClientUserUpdateEntity);
        log.info("远程调用service-middleware修改用户昵称结果："+result);
        return resClientUserInfoVO;
    }


}
