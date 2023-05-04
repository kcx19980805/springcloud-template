package com.kcx.api.admin.sys.responseVo;

import com.kcx.common.utils.page.ResPageBaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 账号管理 列表
 */
@Data
public class ResUserListVO extends ResPageBaseVO {

    @ApiModelProperty(value = "账号id")
    private String oid;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "状态(0正常 1禁用)")
    private String status;
}
