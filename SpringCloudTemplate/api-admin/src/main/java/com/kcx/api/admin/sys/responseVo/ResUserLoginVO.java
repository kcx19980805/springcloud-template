package com.kcx.api.admin.sys.responseVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author kcx
 * @description: 用户登录
 * @date 2023/5/2 16:33
 */
@Data
public class ResUserLoginVO {

    @ApiModelProperty("唯一标识,后续请求在请求头中api-admin-token携带")
    private String token;

}
