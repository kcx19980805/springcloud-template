package com.kcx.task.producer.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcx.common.entity.database1.ClientUserEntity;
import com.kcx.task.producer.sys.requestVo.ReqClientUserUpdateVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户管理
 */
@Mapper
public interface ClientUserDao extends BaseMapper<ClientUserEntity> {


    int updateNickName(ReqClientUserUpdateVO req);
}
