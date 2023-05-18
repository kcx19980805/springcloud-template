package com.kcx.api.user.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcx.api.user.sys.requestVo.ReqSysDictDataListVO;
import com.kcx.api.user.sys.responseVo.ResSysDictDataListVO;
import com.kcx.common.entity.database1.SysDictDataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.websocket.server.PathParam;
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
