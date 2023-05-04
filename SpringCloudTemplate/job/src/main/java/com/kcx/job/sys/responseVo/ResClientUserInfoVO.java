package com.kcx.job.sys.responseVo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户管理-单个信息
 *
 * @author
 * @email
 * @date 2021-03-08 14:12:52
 */
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
