package com.kcx.common.utils.page.responseVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页数据响应格式
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResPageDataVO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("数据列表")
    private List<T> list;

    @ApiModelProperty("数据总数")
    private long total;

    public void setList(List list) {
        if(list == null){
            this.list = new ArrayList();
        }else{
            this.list = list;
        }
    }
}
