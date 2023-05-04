package com.kcx.service.middleware.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kcx.common.entity.database1.ClientUserEntity;
import com.kcx.common.utils.Result;
import com.kcx.service.middleware.sys.requestVo.ReqClientUserUpdateVO;


/**
 * 用户管理
 */
public interface ClientUserService extends IService<ClientUserEntity> {

    /**
     * 修改用户昵称
     */
    Result<String> updateNickName(ReqClientUserUpdateVO req);
}

