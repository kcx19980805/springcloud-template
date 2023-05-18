package com.kcx.service.middleware.sys.requestVo;

import com.kcx.common.entityElasticSearch.Product;
import com.kcx.common.utils.regular.RegUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 修改商品
 */
@Data
public class ReqProductUpdateVO {
    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private String id;
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
