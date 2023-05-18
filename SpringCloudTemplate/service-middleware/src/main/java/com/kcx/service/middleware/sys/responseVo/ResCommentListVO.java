package com.kcx.service.middleware.sys.responseVo;

import com.kcx.common.utils.page.responseVo.ResPageBaseVO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 */
@Data
public class ResCommentListVO extends ResPageBaseVO {

    private String id;
    /**
     * 吐槽内容,该属性对应mongodb的字段的名字，如果一致，则无需该注解
     */
    private String content;
    /**
     * 发布日期
     */
    private LocalDateTime publishTime;
    /**
     * 发布人ID,添加索引
     */
    private String userid;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 评论的日期时间
     */
    private LocalDateTime createTime;
    /**
     * 点赞数
     */
    private Integer likeNum;
    /**
     * 回复数
     */
    private Integer replyNum;
    /**
     * 状态
     */
    private String state;
    /**
     * 上级ID
     */
    private String parentId;
}
