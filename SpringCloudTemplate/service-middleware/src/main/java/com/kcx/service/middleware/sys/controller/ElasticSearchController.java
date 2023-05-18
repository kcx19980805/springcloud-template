package com.kcx.service.middleware.sys.controller;

import com.kcx.common.entity.common.ReqIdEntity;
import com.kcx.common.entityElasticSearch.Product;
import com.kcx.common.utils.Result;
import com.kcx.common.utils.elasticsearch.ElasticsearchUtils;
import com.kcx.common.utils.page.responseVo.ResPageDataVO;
import com.kcx.service.middleware.sys.requestVo.*;
import com.kcx.service.middleware.sys.serviceElasticsearch.ProductService;
import com.kcx.service.middleware.sys.serviceMongo.CommentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author kcx 2023/5/17 22:12
 */
@RestController
public class ElasticSearchController {

    @Resource
    ProductService productService;

    @PostMapping("product/saveProduct")
    public Result<String> saveProduct(@RequestBody @Validated ReqProductAddVO vo){
        if(productService.saveProduct(vo)){
            return Result.success();
        }
        return Result.error();
    }

    @PostMapping("product/findById")
    public Result<Product> findById(@RequestBody @Validated ReqIdEntity reqIdEntity){
        return Result.success(productService.findById(reqIdEntity.getId()));
    }

    @PostMapping("product/findList")
    public Result<ResPageDataVO<Product>> findList(@RequestBody @Validated ReqProductListVO vo){
        return Result.success(productService.findList(vo));
    }

    @PostMapping("product/findList2")
    public Result<ResPageDataVO<Product>> findList2(@RequestBody @Validated ReqProductListVO vo){
        return Result.success(productService.findList2(vo));
    }

    @PostMapping("product/updateById")
    public Result<String> updateById(@RequestBody @Validated ReqProductUpdateVO vo){
        if(productService.updateById(vo)){
            return Result.success();
        }
        return Result.error();
    }

    @PostMapping("product/deleteById")
    public Result<String> deleteById(@RequestBody @Validated ReqIdEntity reqIdEntity) {
        if(productService.deleteById(reqIdEntity.getId())){
            return Result.success();
        }
        return Result.error();
    }
}
