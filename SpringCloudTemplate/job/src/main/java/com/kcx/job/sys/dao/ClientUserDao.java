package com.kcx.job.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcx.common.entity.database1.ClientUserEntity;
import com.kcx.job.sys.responseVo.ResClientUserInfoVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户管理
 */
@Mapper
public interface ClientUserDao extends BaseMapper<ClientUserEntity> {

    ResClientUserInfoVO userInfo(String userId);

}
