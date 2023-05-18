package com.kcx.service.middleware.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcx.common.entity.database1.SysDictDataEntity;
import com.kcx.common.entityFegin.apiUser.requestVo.ReqSysDictDataListVO;
import com.kcx.common.entityFegin.apiUser.responseVo.ResSysDictDataListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统字典数据表
 */
@Mapper
public interface SysDictDataDao extends BaseMapper<SysDictDataEntity> {

    /**
     * 账号管理-列表-查询
     *
     * @param req
     * @return
     */
    List<ResSysDictDataListVO> list(ReqSysDictDataListVO req);

    /**
     * 账号管理-列表-查询 总数
     *
     * @param req
     * @return
     */
    long listTotal(ReqSysDictDataListVO req);

}
