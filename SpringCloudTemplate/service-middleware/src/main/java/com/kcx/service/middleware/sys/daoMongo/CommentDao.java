package com.kcx.service.middleware.sys.daoMongo;

import com.kcx.common.entityMongo.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * spring-data操作mongo
 */
public interface CommentDao extends MongoRepository<Comment,String> {

    @Query("{nickname:?0}")
    Page<Comment> findList(String nickname, Pageable pageable);

}
