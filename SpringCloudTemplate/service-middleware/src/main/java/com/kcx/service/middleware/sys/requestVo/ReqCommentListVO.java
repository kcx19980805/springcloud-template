package com.kcx.service.middleware.sys.requestVo;

import com.kcx.common.utils.page.requestVo.ReqPageBaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 评论列表
 */
@Data
public class ReqCommentListVO  extends ReqPageBaseVO {
    @ApiModelProperty(value = "昵称")
    private String nickname;
}
