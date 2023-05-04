package com.kcx.api.user.sys.dao;

import com.kcx.common.entity.database1.ClientUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户管理
 */
@Mapper
public interface ClientUserDao extends BaseMapper<ClientUserEntity> {
}
