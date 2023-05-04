package com.kcx.api.admin.sys.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kcx.api.admin.config.jwt.JWTConfig;
import com.kcx.api.admin.sys.dao.SysUserDao;

import com.kcx.api.admin.sys.feign.ApiUserFeign;
import com.kcx.api.admin.sys.requestVo.*;
import com.kcx.api.admin.sys.responseVo.ResUserListVO;
import com.kcx.api.admin.sys.responseVo.ResUserLoginVO;
import com.kcx.api.admin.sys.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcx.common.constant.HttpStatus;
import com.kcx.common.entity.database1.SysUserEntity;
import com.kcx.common.entityFegin.apiUser.requestVo.ReqSysDictDataListVO;
import com.kcx.common.entityFegin.apiUser.responseVo.ResSysDictDataListVO;
import com.kcx.common.exception.CustomException;
import com.kcx.common.utils.Result;
import com.kcx.common.utils.encryption.SHAUtils;
import com.kcx.common.utils.page.PageUtils;
import com.kcx.common.utils.page.ResPageDataVO;
import com.kcx.common.utils.token.JWTUtils;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 账号管理(平台)
 *
 * @author
 * @email
 * @date 2020-12-03 14:40:12
 */
@Service("sysUserService")
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    @Resource
    private ApiUserFeign apiUserFeign;

    @Resource
    private JWTConfig jwtConfig;

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @DS("slave1")
    public ResUserLoginVO userLogin(ReqUserLoginVO req) {
        ReqSysDictDataListVO reqSysDictDataListVO = new ReqSysDictDataListVO();
        reqSysDictDataListVO.setLimit(10);
        reqSysDictDataListVO.setPage(1);
        Result<ResPageDataVO<ResSysDictDataListVO>> resPageDataVOResult = apiUserFeign.list(reqSysDictDataListVO);
        log.info("远程调用api-user结果："+resPageDataVOResult);
        if(resPageDataVOResult.getCode() != HttpStatus.SUCCESS){
            throw new CustomException("远程调用api-user异常");
        }
        SysUserEntity sysUserEntity = baseMapper.selectOne(new QueryWrapper<SysUserEntity>().eq("account", req.getAccount()));
        if(null == sysUserEntity){
            throw new CustomException("账号不存在");
        }
        boolean b = SHAUtils.checkPasswordSha256(sysUserEntity.getPassword(),req.getPassword(),sysUserEntity.getSalt());
        if(!b){
            throw new CustomException("密码错误");
        }
        String token = JWTUtils.getToken(jwtConfig,sysUserEntity.getOid());
        ResUserLoginVO resUserLoginVO = new ResUserLoginVO();
        resUserLoginVO.setToken(token);
        return resUserLoginVO;
    }

    @Override
    public ResPageDataVO<ResUserListVO> userList(ReqUserListVO req) {
        return PageUtils.autoPageData(()->baseMapper.userListTotal(req),()->baseMapper.userList(req),req);
    }

}

