package com.kcx.api.admin.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcx.common.entity.database1.SysOperLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志
 */
@Mapper
public interface SysOperLogDao extends BaseMapper<SysOperLogEntity> {

}
