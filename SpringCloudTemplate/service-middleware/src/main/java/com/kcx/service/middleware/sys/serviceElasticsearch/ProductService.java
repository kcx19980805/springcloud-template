package com.kcx.service.middleware.sys.serviceElasticsearch;

import com.kcx.common.entityElasticSearch.Product;
import com.kcx.common.entityMongo.Comment;
import com.kcx.common.utils.page.responseVo.ResPageDataVO;
import com.kcx.service.middleware.sys.requestVo.*;

import java.util.List;

/**
 * 商品es
 */
public interface ProductService {

    boolean saveProduct(ReqProductAddVO vo);

    Product findById(String id);

    ResPageDataVO<Product> findList(ReqProductListVO req);

    ResPageDataVO<Product> findList2(ReqProductListVO req);

    boolean updateById(ReqProductUpdateVO req);

    boolean deleteById(String id);
}
