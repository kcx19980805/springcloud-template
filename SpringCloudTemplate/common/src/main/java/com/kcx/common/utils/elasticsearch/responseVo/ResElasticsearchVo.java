package com.kcx.common.utils.elasticsearch.responseVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

/**
 * Elasticsearch数据响应格式
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResElasticsearchVo {

    @ApiModelProperty("数据总数")
    private Long total;

    @ApiModelProperty("数据列表,key为自带的_id,value为剩余字段")
    private LinkedHashMap<String,String> data;

    public void setData(LinkedHashMap data) {
        this.data = data;
    }
}
