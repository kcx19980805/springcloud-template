package com.kcx.api.user.sys.service.impl;

import com.kcx.api.user.config.jwt.JWTConfig;
import com.kcx.api.user.sys.dao.ClientUserDao;
import com.kcx.api.user.sys.responseVo.ResSendSmsVO;
import com.kcx.common.entity.database1.ClientUserEntity;
import com.kcx.api.user.sys.requestVo.*;
import com.kcx.api.user.sys.responseVo.ResLoginVO;
import com.kcx.api.user.sys.service.ClientUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcx.common.exception.CustomException;
import com.kcx.common.utils.Result;
import com.kcx.common.utils.encryption.SHAUtils;
import com.kcx.common.utils.id.SnowFlakeIdUtils;
import com.kcx.common.utils.random.RandomUtils;
import com.kcx.common.utils.redis.RedisUtils;
import com.kcx.common.utils.sms.SmsUtils;
import com.kcx.common.utils.token.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理
 *
 * @author
 * @email
 * @date 2020-12-03 14:40:12
 */
@Service("clientUserService")
public class ClientUserServiceImpl extends ServiceImpl<ClientUserDao, ClientUserEntity> implements ClientUserService {

    @Resource
    private JWTConfig jwtConfig;
    @Resource
    private RedisUtils redisUtils;
    @Override
    public ResSendSmsVO sendSms(ReqSendSmsVO req) {
        ResSendSmsVO resSendSmsVO = new ResSendSmsVO();
        try {
            String code = String.valueOf(RandomUtils.randomNumLong(4));
            boolean b = SmsUtils.sendAliSms(req.getPhone(), code);
            if(b){
                redisUtils.setCacheObject(req.getPhone(),code,5, TimeUnit.MINUTES);
            }else{
                resSendSmsVO.setMsg("验证码发送失败");
                return resSendSmsVO;
            }
            resSendSmsVO.setMsg("验证码已发送，5分钟内有效");
        }catch (Exception e){
            e.printStackTrace();
            resSendSmsVO.setMsg("验证码发送失败");
        }
        return resSendSmsVO;
    }

    @Override
    public Result<String> register(ReqRegisterVO reqRegisterVO) {
        Object code = redisUtils.getCacheObject(reqRegisterVO.getPhone());
        if(code == null){
            throw new CustomException("验证码已失效");
        }
        if(!String.valueOf(code).equalsIgnoreCase(reqRegisterVO.getCode())){
            throw new CustomException("验证码不正确");
        }
        ClientUserEntity clientUserEntity = new ClientUserEntity();
        clientUserEntity.setOid(String.valueOf(SnowFlakeIdUtils.getSnowflakeId()));
        clientUserEntity.setAccount(reqRegisterVO.getPhone());
        String salt = SHAUtils.getGenerateSalt();
        clientUserEntity.setPassword(SHAUtils.sha256(reqRegisterVO.getPassword(),salt));
        clientUserEntity.setSalt(salt);
        clientUserEntity.setIsDel("0");
        redisUtils.deleteObject(reqRegisterVO.getPhone());
        return Result.affectedRows(baseMapper.insert(clientUserEntity));
    }


    @Override
    public ResLoginVO login(ReqLoginVO req) {
        String account = req.getAccount();
        String password = req.getPassword();
        ClientUserEntity clientUser= baseMapper.selectOne(new QueryWrapper<ClientUserEntity>().eq("account", account).eq("is_del", 0));
        if (clientUser == null) {
            throw new CustomException("账号不存在");
        }
        if ("1".equals(clientUser.getStatus())) {
            throw new CustomException("账号已被禁用");
        }
        boolean isCorrect=false;
        if(StringUtils.isNotBlank(password)){
            if(!SHAUtils.checkPasswordSha256(clientUser.getPassword(), password, clientUser.getSalt())){
                throw new CustomException("密码错误");
            }
        } else if(StringUtils.isNotBlank(req.getCode())){
            Object code = redisUtils.getCacheObject(account);
            if(code == null){
                throw new CustomException("验证码已失效");
            }
            if(!req.getCode().equalsIgnoreCase(String.valueOf(code))){
                throw new CustomException("验证码不正确");
            }
        }else{
            throw new CustomException("验证码或密码为空");
        }
        //用户id
        String userId = clientUser.getOid().toString();
        //生成token
        String token = JWTUtils.getToken(jwtConfig,userId);
        ResLoginVO responseLoginEntity = new ResLoginVO();
        responseLoginEntity.setUserId(clientUser.getOid().toString());
        responseLoginEntity.setToken(token);
        responseLoginEntity.setNickName(clientUser.getNickName());
        responseLoginEntity.setHeadImg(clientUser.getHeadImg());
        return responseLoginEntity;
    }


}

