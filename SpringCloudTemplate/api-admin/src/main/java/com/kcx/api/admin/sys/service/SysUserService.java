package com.kcx.api.admin.sys.service;

import com.kcx.api.admin.sys.requestVo.*;
import com.kcx.api.admin.sys.responseVo.ResUserListVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kcx.api.admin.sys.responseVo.ResUserLoginVO;
import com.kcx.common.entity.database1.SysUserEntity;
import com.kcx.common.utils.page.ResPageDataVO;

/**
 * 账号管理(平台)
 *
 * @author
 * @email
 * @date 2020-12-03 14:40:12
 */
public interface SysUserService extends IService<SysUserEntity> {

    /**
     * 登录
     */
    ResUserLoginVO userLogin(ReqUserLoginVO req);

    /**
     * 查询所有账号列表
     */
    ResPageDataVO<ResUserListVO> userList(ReqUserListVO requestUserListEntity);

}

