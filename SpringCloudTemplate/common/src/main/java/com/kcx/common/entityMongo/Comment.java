package com.kcx.common.entityMongo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * 文章评论实体类
 */
//把一个java类声明为mongodb的文档，调用方法时自动创建，可以通过collection参数指定这个类对应的文档。可以省略，如果省略，则默认使用类名小写映射集合
@Document(collection = "comment")
//复合索引
// @CompoundIndex( def = "{'userid': 1, 'nickname': -1}")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    /**
     * 主键标识，该属性的值会自动对应mongodb的主键字段"_id"，如果该属性名就叫“id”,则该注解可以省略，否则必须写
     */
    @Id
    private String id;
    /**
     * 吐槽内容,该属性对应mongodb的字段的名字，如果一致，则无需该注解
     */
    @Field("content")
    private String content;
    /**
     * 发布日期
     */
    private String publishTime;
    /**
     * 发布人ID,添加索引
     */
    @Indexed
    private String userid;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 评论的日期时间
     */
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
