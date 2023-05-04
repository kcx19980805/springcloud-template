package com.kcx.api.user.sys.service;

import com.kcx.api.user.sys.responseVo.ResSendSmsVO;
import com.kcx.common.entity.database1.ClientUserEntity;
import com.kcx.api.user.sys.requestVo.*;
import com.kcx.api.user.sys.responseVo.ResLoginVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kcx.common.utils.Result;

/**
 * 用户管理
 *
 * @author
 * @email
 * @date 2020-12-03 14:40:12
 */
public interface ClientUserService extends IService<ClientUserEntity> {

    /**
     * 发送短信
     * @param req
     * @return
     */
    ResSendSmsVO sendSms(ReqSendSmsVO req);

    /**
     * 注册
     * @param reqRegisterVO
     * @return
     */
    Result<String> register(ReqRegisterVO reqRegisterVO);

    /**
     * 登录
     * @param req
     * @return
     */
    ResLoginVO login(ReqLoginVO req);

}

