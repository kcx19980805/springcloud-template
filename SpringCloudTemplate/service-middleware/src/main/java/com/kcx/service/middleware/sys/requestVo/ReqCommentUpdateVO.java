package com.kcx.service.middleware.sys.requestVo;

import com.kcx.common.utils.regular.RegUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 修改评论
 */
@Data
public class ReqCommentUpdateVO {
    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @Pattern(regexp = RegUtils.DateTime,message = "createTime格式不正确")
    @ApiModelProperty(value = "评论的日期时间")
    private String createTime;

    @ApiModelProperty(value = "点赞数")
    private String likeNum;
}
