package com.kcx.common.entityFegin.job.requestVo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReqClientUserVO {
    @NotBlank(message = "用户id不能为空")
    private String userId;
}
