package com.kcx.service.middleware.sys.requestVo;

import com.kcx.common.entityElasticSearch.Product;
import com.kcx.common.utils.regular.RegUtils;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 新增商品
 */
@Data
public class ReqProductAddVO {
    /**
     * 商品名称
     */
    private String title;
    /**
     * 分类名称
     */
    private String category;
    /**
     * 商品价格
     */
    @Pattern(regexp = RegUtils.Money,message = "价格格式不正确")
    private String price;
    /**
     * 图片地址
     */
    @Pattern(regexp = RegUtils.Url,message = "图片地址不正确")
    private String images;
    /**
     * 商品规格
     */
    private List<Product.Sku> skus;
}
