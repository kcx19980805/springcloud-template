package com.kcx.job.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kcx.common.entity.database1.ClientUserEntity;
import com.kcx.job.sys.requestVo.ReqClientUserVO;
import com.kcx.job.sys.responseVo.ResClientUserInfoVO;

/**
 * 用户管理
 *
 * @author
 * @email
 * @date 2020-12-03 14:40:12
 */
public interface ClientUserService extends IService<ClientUserEntity> {

    /**
     * 查询指定用户信息
     * @param req
     * @return
     */
    ResClientUserInfoVO getUserInfo(ReqClientUserVO req);
}

