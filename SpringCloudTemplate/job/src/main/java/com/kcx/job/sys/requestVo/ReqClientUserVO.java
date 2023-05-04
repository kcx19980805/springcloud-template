package com.kcx.job.sys.requestVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author kcx
 * @description: 用户管理-单个信息
 * @date 2022/5/4 19:08
 */
@Data
public class ReqClientUserVO {
    @NotBlank(message = "用户id不能为空")
    private String userId;
}
