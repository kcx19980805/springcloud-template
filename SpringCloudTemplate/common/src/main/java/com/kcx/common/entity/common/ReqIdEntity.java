package com.kcx.common.entity.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 根据id查询或删除
 */
@Data
public class ReqIdEntity {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;
}
