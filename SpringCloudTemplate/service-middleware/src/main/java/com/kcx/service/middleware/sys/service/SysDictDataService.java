package com.kcx.service.middleware.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.kcx.common.entity.database1.SysDictDataEntity;
import com.kcx.common.entityFegin.apiUser.requestVo.ReqSysDictDataListVO;
import com.kcx.common.entityFegin.apiUser.responseVo.ResSysDictDataListVO;
import com.kcx.common.utils.page.responseVo.ResPageDataVO;


/**
 * 系统字典数据表
 */
public interface SysDictDataService extends IService<SysDictDataEntity> {

    /**
     * 查询所有账号列表
     */
    ResPageDataVO<ResSysDictDataListVO> list(ReqSysDictDataListVO requestUserListEntity);
}
