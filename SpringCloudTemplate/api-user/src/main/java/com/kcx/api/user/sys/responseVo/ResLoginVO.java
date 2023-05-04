package com.kcx.api.user.sys.responseVo;

import com.kcx.common.constant.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录
 */
@Data
public class ResLoginVO {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String headImg;

    @ApiModelProperty("访问令牌")
    private String token;

}
