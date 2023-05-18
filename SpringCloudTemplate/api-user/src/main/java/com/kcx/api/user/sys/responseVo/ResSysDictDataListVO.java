package com.kcx.api.user.sys.responseVo;

import com.kcx.common.utils.page.responseVo.ResPageBaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author kcx
 * @description:
 * @date 2023/5/2 18:29
 */
@Data
public class ResSysDictDataListVO extends ResPageBaseVO {
    /**
     * 分布式唯一id（分库分表后唯一）
     */
    @ApiModelProperty("分布式唯一id（分库分表后唯一）")
    private String oid;
    /**
     * 字典排序
     */
    @ApiModelProperty("字典排序")
    private Integer dictSort;
    /**
     * 字典标签
     */
    @ApiModelProperty("字典标签")
    private String dictLabel;
    /**
     * 字典键值
     */
    @ApiModelProperty("字典键值")
    private String dictValue;
    /**
     * 字典类型
     */
    @ApiModelProperty("字典类型")
    private String dictType;

}
