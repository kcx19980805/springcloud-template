package com.kcx.api.user.sys.responseVo;

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
public class ResSendSmsVO {
    @ApiModelProperty(value = "结果提示", required = true)
    private String msg;
}
