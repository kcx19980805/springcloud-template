package com.kcx.service.middleware.sys.requestVo;

import com.kcx.common.utils.regular.RegUtils;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @author kcx 2023/5/10 20:37
 */
@Data
public class ReqCommentAddVO {
    /**
     * 吐槽内容,该属性对应mongodb的字段的名字，如果一致，则无需该注解
     */
    private String content;
    /**
     * 发布日期
     */
    @Pattern(regexp = RegUtils.DateTime)
    private String publishTime;
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
    @Pattern(regexp = RegUtils.DateTime,message = "评论日期格式不正确")
    private String createTime;
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
    /**
     * 文章id
     */
    private String articleId;
}
