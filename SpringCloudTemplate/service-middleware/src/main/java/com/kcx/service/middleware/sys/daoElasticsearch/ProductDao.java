package com.kcx.service.middleware.sys.daoElasticsearch;

/**
 * spring-data操作elasticsearch
 */
import com.kcx.common.entityElasticSearch.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends ElasticsearchRepository<Product,Long> {

    /**
     * 定义一个方法查询：根据title查询
     * 原因：  ElasticsearchRepository会分析方法名,参数对应es中的field（这就是灵活之处）
     */
    List<Product> findByTitle(String title);

    /**
     * 手动写查询语句，分页查询
     * @param category
     * @param pageable
     * @return
     */
    @Query("{\"bool\" : {\"must\" : {\"match\" : {\"category\" : \"?0\"}}}}")
    Page<Product> findByCategory(String category, Pageable pageable);
}
