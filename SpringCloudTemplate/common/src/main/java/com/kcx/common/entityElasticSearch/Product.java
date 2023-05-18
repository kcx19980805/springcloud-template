package com.kcx.common.entityElasticSearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.List;

/**
 * 商品实体类
 */
//声明索引名称，调用方法时自动创建
@Document(indexName = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
//分片和副本集
@Setting(shards = 3, replicas = 1)
public class Product {
    //必须有 id,这里的 id 是全局唯一的标识，等同于 es 中的"_id"
    @Id
    private Long id;//商品唯一标识
    /**
     * 商品名称
     * type : 字段数据类型
     * analyzer : 分词器类型
     * index : 是否索引(默认:true)
     * Keyword : 短语,不进行分词
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;
    /**
     * 分类名称
     */
    @Field(type = FieldType.Keyword)
    private String category;
    /**
     * 商品价格
     */
    @Field(type = FieldType.Double)
    private Double price;
    /**
     * 图片地址
     */
    @Field(type = FieldType.Keyword, index = false)
    private String images;

    @Field(type = FieldType.Nested)
    private List<Sku> skus;

    /**
     * 商品规格
     */
    @Data
    public static class Sku{
        /**
         * 规格名称
         */
        @Field(type = FieldType.Keyword)
        private String skuName;
        /**
         * 规格值
         */
        @Field(type = FieldType.Keyword)
        private String skuValue;
    }
}
