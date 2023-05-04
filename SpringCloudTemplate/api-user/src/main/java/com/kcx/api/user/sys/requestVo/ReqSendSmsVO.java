package com.kcx.api.user.sys.requestVo;

import com.kcx.common.utils.regular.RegUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author kcx
 * @description: 发送短信
 * @date 2023/5/2 17:27
 */
@Data
public class ReqSendSmsVO {
    @ApiModelProperty(value = "账号（手机号）", required = true)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = RegUtils.Phone,message = "手机号格式不正确")
    private String phone;
}
