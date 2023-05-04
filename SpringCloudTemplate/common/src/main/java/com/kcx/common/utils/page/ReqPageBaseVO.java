package com.kcx.common.utils.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 分页请求基类，用于前端传参，前端实体类可继承此类
 */
@Data
public class ReqPageBaseVO {

    @ApiModelProperty(value = "当前页数，前端传(从1开始)", required = true)
    @NotNull(message = "page不能为空")
    public Integer page;

    @ApiModelProperty(value = "sql起始页数，实际sql使用(page-1)*limit", hidden = true)
    public Integer sqlPage;

    @ApiModelProperty("每页显示记录数(为空默认查询10条,-1查询全部)")
    public Integer limit;

    @ApiModelProperty(value = "序号排序(asc升序 desc降序)为空默认升序")
    public String order;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit == null ? 10 : limit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        // 前端传值page从1开始 ,但sql是从0开始
        if (page == null || page <= 0) {
            this.page = 0;
            this.sqlPage = 0;
        } else {
            this.page = page;
            this.sqlPage = (page - 1) * limit;
        }
    }

    public Integer getSqlPage() {
        return sqlPage;
    }

    public void setSqlPage(Integer sqlPage) {
        this.sqlPage = sqlPage;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = StringUtils.isBlank(order) ? "asc" : order;
    }
}
