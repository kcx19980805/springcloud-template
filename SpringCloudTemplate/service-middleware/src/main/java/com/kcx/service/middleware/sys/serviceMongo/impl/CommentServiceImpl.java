package com.kcx.service.middleware.sys.serviceMongo.impl;

import com.kcx.common.entityMongo.Comment;
import com.kcx.common.utils.bean.BeanConvertUtils;
import com.kcx.common.utils.mongo.MongoDBUtils;
import com.kcx.common.utils.page.PageUtils;
import com.kcx.common.utils.page.responseVo.ResPageDataVO;
import com.kcx.service.middleware.sys.daoMongo.CommentDao;
import com.kcx.service.middleware.sys.requestVo.ReqCommentAddVO;
import com.kcx.service.middleware.sys.requestVo.ReqCommentListVO;
import com.kcx.service.middleware.sys.requestVo.ReqCommentUpdateVO;
import com.kcx.service.middleware.sys.responseVo.ResCommentListVO;
import com.kcx.service.middleware.sys.serviceMongo.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**
 * MongoDB操作
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private CommentDao commentDao;

    @Override
    public boolean saveComment(ReqCommentAddVO vo) {
        Comment comment = BeanConvertUtils.dataToEntity(vo,Comment.class);
        return MongoDBUtils.insert(comment,mongoTemplate);
    }

    @Override
    public Comment findById(String id) {
        return MongoDBUtils.selectById(id,Comment.class,mongoTemplate);
    }

    @Override
    public ResPageDataVO<Comment> findList(ReqCommentListVO req) {
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        int currentPage = 0;
        int pageSize = 2;
        //设置查询分页
        Pageable pageable = PageRequest.of(currentPage, pageSize, sort);
        Page<Comment> commentDaoList = commentDao.findList(req.getNickname(), pageable);
        System.err.println(commentDaoList.getContent());
        System.err.println(commentDaoList.getTotalElements());

        Comment comment = BeanConvertUtils.dataToEntity(req,Comment.class);
            return PageUtils.autoPageData(() -> MongoDBUtils.selectCount(comment, mongoTemplate),
                    () -> MongoDBUtils.selectList(comment,req.getSqlPage(),req.getLimit(), ResCommentListVO.class,mongoTemplate),req);
    }

    @Override
    public long updateById(ReqCommentUpdateVO req) {
        Comment comment = BeanConvertUtils.dataToEntity(req,Comment.class);
        return MongoDBUtils.updateById(req.getId(),comment,mongoTemplate);
    }

    @Override
    public long deleteById(String id) {
        return MongoDBUtils.deleteById(id,Comment.class,mongoTemplate);
    }

}
