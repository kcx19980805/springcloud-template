package com.kcx.service.middleware.sys.serviceElasticsearch.impl;

import com.alibaba.fastjson.JSONObject;
import com.kcx.common.entityElasticSearch.Product;
import com.kcx.common.utils.bean.BeanConvertUtils;
import com.kcx.common.utils.elasticsearch.ElasticsearchUtils;
import com.kcx.common.utils.elasticsearch.requestVo.ReqElasticsearchVo;
import com.kcx.common.utils.elasticsearch.responseVo.ResElasticsearchVo;
import com.kcx.common.utils.id.SnowFlakeIdUtils;
import com.kcx.common.utils.page.responseVo.ResPageDataVO;
import com.kcx.service.middleware.sys.daoElasticsearch.ProductDao;
import com.kcx.service.middleware.sys.requestVo.ReqProductAddVO;
import com.kcx.service.middleware.sys.requestVo.ReqProductListVO;
import com.kcx.service.middleware.sys.requestVo.ReqProductUpdateVO;
import com.kcx.service.middleware.sys.serviceElasticsearch.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Elasticsearch操作
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductDao productDao;
    @Resource
    private RestHighLevelClient elasticsearchClient;

    @Override
    public boolean saveProduct(ReqProductAddVO vo) {
        Product product = BeanConvertUtils.dataToEntity(vo,Product.class);
        product.setId(SnowFlakeIdUtils.getSnowflakeId());
        Product save = productDao.save(product);
        System.out.println(save);
        if(save.getId()!=null){
            return true;
        }
        return false;
    }

    @Override
    public Product findById(String id) {
        Product product = ElasticsearchUtils.selectById(id, Product.class, elasticsearchClient);
        return product;
    }

    @Override
    public ResPageDataVO<Product> findList(ReqProductListVO req) {
        Product product = BeanConvertUtils.dataToEntity(req,Product.class);
        System.out.println(product);
        ResElasticsearchVo resElasticsearchVo = ElasticsearchUtils.selectList(product, req.getSqlPage().intValue(), req.getLimit(), elasticsearchClient);
        return ElasticsearchUtils.convertVoToPage(resElasticsearchVo,Product.class);
    }

    @Override
    public ResPageDataVO<Product> findList2(ReqProductListVO req) {
        ReqElasticsearchVo vo = new ReqElasticsearchVo();
        Map<String, Object> mustTermMap = new LinkedHashMap<>();
        Map<String, Object> mustMatchMap = new LinkedHashMap<>();
        if(StringUtils.isNotBlank(req.getTitle())){
            mustMatchMap.put("title",req.getTitle());
        }
        List<Product.Sku> skus = req.getSkus();
        if(null != skus && skus.size()>0){
            for(Product.Sku sku : skus){
                if(StringUtils.isNotBlank(sku.getSkuName())){
                    mustTermMap.put("skus.skuName",sku.getSkuName());
                }
                if(StringUtils.isNotBlank(sku.getSkuValue())){
                    mustTermMap.put("skus.skuValue",sku.getSkuValue());
                }
            }
        }
        vo.setMustTermMap(mustTermMap);
        vo.setMustMatchMap(mustMatchMap);
        vo.setPageNo(req.getPage().intValue());
        vo.setPageSize(req.getLimit());
        System.out.println(vo);
        ResElasticsearchVo resElasticsearchVo = ElasticsearchUtils.selectFull(Product.class,vo,elasticsearchClient);
        return ElasticsearchUtils.convertVoToPage(resElasticsearchVo,Product.class);
    }

    @Override
    public boolean updateById(ReqProductUpdateVO req) {
        Product product = BeanConvertUtils.dataToEntity(req,Product.class);
        System.out.println(product);
        return ElasticsearchUtils.updateById(product,true,elasticsearchClient);
    }

    @Override
    public boolean deleteById(String id) {
        return ElasticsearchUtils.deleteById(id,Product.class,elasticsearchClient);
    }

}
