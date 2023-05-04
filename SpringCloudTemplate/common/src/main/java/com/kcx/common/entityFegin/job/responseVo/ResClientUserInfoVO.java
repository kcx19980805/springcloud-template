package com.kcx.common.entityFegin.job.responseVo;

import lombok.Data;

@Data
public class ResClientUserInfoVO {
    /**
     * 分布式唯一id（分库分表后唯一）
     */
    private String oid;
    /**
     * 登录账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 昵称
     */
    private String nickName;
}
