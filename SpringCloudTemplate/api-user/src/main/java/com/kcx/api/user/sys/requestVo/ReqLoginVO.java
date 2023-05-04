package com.kcx.api.user.sys.requestVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录
 * @author
 * @email
 * @date 2020-12-03 14:40:14
 */
@Data
@ApiModel
public class ReqLoginVO {

    @NotBlank(message = "账号不能为空")
    @ApiModelProperty(value = "账号(手机号)", required = true)
    private String account;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "验证码")
    private String code;
}
