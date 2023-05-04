package com.kcx.common.entityFegin.taskProducer.requestVo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改用户昵称
 */
@Data
public class ReqClientUserUpdateVO {
    @NotBlank(message = "用户id不能为空")
    private String oid;

    @NotBlank(message = "昵称不能为空")
    private String nickName;
}
