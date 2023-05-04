package com.kcx.service.third.sys.requestVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改用户昵称
 */
@Data
public class ReqClientUserUpdateVO {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id",required = true)
    private String oid;

    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty(value = "昵称",required = true)
    private String nickName;
}
