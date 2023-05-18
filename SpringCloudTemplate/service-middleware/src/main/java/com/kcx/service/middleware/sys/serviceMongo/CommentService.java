package com.kcx.service.middleware.sys.serviceMongo;

import com.kcx.common.entityMongo.Comment;
import com.kcx.common.utils.page.responseVo.ResPageDataVO;
import com.kcx.service.middleware.sys.requestVo.ReqCommentAddVO;
import com.kcx.service.middleware.sys.requestVo.ReqCommentListVO;
import com.kcx.service.middleware.sys.requestVo.ReqCommentUpdateVO;

/**
 * 评论mongo
 */
public interface CommentService {

    boolean saveComment(ReqCommentAddVO vo);

    Comment findById(String id);

    ResPageDataVO<Comment> findList(ReqCommentListVO vo);

    long updateById(ReqCommentUpdateVO req);

    long deleteById(String id);
}
