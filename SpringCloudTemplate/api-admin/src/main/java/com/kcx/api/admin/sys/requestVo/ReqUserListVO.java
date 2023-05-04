package com.kcx.api.admin.sys.requestVo;

import com.kcx.common.utils.page.ReqPageBaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 账号管理列表
 */
@Data
public class ReqUserListVO extends ReqPageBaseVO {

    @ApiModelProperty(value = "账号关键字")
    public String keyword;

}
