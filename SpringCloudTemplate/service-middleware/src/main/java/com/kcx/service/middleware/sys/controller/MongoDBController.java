package com.kcx.service.middleware.sys.controller;

import com.kcx.common.entity.common.ReqIdEntity;
import com.kcx.common.entityMongo.Comment;
import com.kcx.common.utils.Result;
import com.kcx.common.utils.page.responseVo.ResPageDataVO;
import com.kcx.service.middleware.sys.requestVo.ReqCommentAddVO;
import com.kcx.service.middleware.sys.requestVo.ReqCommentListVO;
import com.kcx.service.middleware.sys.requestVo.ReqCommentUpdateVO;
import com.kcx.service.middleware.sys.serviceMongo.CommentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author kcx 2023/5/10 20:02
 */
@RestController
public class MongoDBController {

    @Resource
    CommentService commentService;

    @PostMapping("comment/saveComment")
    public Result<String> saveComment(@RequestBody @Validated ReqCommentAddVO vo){
        if(commentService.saveComment(vo)){
           return Result.success();
        }
        return Result.error();
    }

    @PostMapping("comment/findById")
    public Result<Comment> findById(@RequestBody @Validated ReqIdEntity reqIdEntity){
        return Result.success(commentService.findById(reqIdEntity.getId()));
    }

    @PostMapping("comment/findList")
    public Result<ResPageDataVO<Comment>> findList(@RequestBody @Validated ReqCommentListVO vo){
        return Result.success(commentService.findList(vo));
    }

    @PostMapping("comment/updateById")
    public Result<String> updateById(@RequestBody @Validated  ReqCommentUpdateVO vo){
        return Result.affectedRows(commentService.updateById(vo));
    }

    @PostMapping("comment/deleteById")
    public Result<String> deleteById(@RequestBody @Validated ReqIdEntity reqIdEntity){
        return Result.affectedRows(commentService.deleteById(reqIdEntity.getId()));
    }
}
