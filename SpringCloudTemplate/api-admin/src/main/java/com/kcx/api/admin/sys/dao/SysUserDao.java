package com.kcx.api.admin.sys.dao;


import com.kcx.api.admin.sys.requestVo.ReqUserListVO;
import com.kcx.api.admin.sys.responseVo.ResUserListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcx.common.entity.database1.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 账号管理(平台)
 *
 * @author
 * @email
 * @date 2020-12-03 14:40:12
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {

    /**
     * 账号管理-列表-查询
     *
     * @param req
     * @return
     */
    List<ResUserListVO> userList(ReqUserListVO req);

    /**
     * 账号管理-列表-查询 总数
     *
     * @param req
     * @return
     */
    long userListTotal(ReqUserListVO req);
}

