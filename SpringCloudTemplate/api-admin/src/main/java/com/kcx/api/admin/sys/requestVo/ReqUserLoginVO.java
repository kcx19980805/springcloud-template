package com.kcx.api.admin.sys.requestVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author kcx
 * @description: 用户登录
 * @date 2023/5/2 16:09
 */
@Data
public class ReqUserLoginVO {

    @ApiModelProperty("账号")
    @NotBlank(message = "account不能为空")
    private String account;

    @ApiModelProperty("密码")
    @NotBlank(message = "password不能为空")
    private String password;

}
