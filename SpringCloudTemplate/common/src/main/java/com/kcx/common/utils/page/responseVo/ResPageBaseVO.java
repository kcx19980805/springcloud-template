package com.kcx.common.utils.page.responseVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页响应基类前端实体类可继承此类
 */
@Data
public class ResPageBaseVO {

    @ApiModelProperty(value = "数据编号，asc则从低到高，desc则从高到底")
    public Long number;

}
