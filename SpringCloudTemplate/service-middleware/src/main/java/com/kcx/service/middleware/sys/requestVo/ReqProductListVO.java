package com.kcx.service.middleware.sys.requestVo;

import com.kcx.common.entityElasticSearch.Product;
import com.kcx.common.utils.page.requestVo.ReqPageBaseVO;
import lombok.Data;

import java.util.List;

/**
 * 商品列表
 */
@Data
public class ReqProductListVO extends ReqPageBaseVO {
    /**
     * 商品名称
     */
    private String title;
    /**
     * 商品规格
     */
    private List<Product.Sku> skus;
}
