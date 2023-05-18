package com.kcx.common.utils.page.requestVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 分页请求基类，用于前端传参，前端实体类可继承此类
 */
@Data
public class ReqPageBaseVO {

    @ApiModelProperty("每页显示记录数(为空默认查询10条,-1查询全部)")
    @NotNull(message = "limit不能为空")
    public Integer limit;

    @ApiModelProperty(value = "当前页数，前端传(从1开始)", required = true)
    @NotNull(message = "page不能为空")
    public Long page;

    @ApiModelProperty(value = "sql起始页数，实际sql使用(page-1)*limit", hidden = true)
    public Long sqlPage;

    @ApiModelProperty(value = "序号排序(asc升序 desc降序)为空默认升序")
    public String order;

    public Long getSqlPage() {
        return sqlPage;
    }

    public void setSqlPage(Long sqlPage) {
        this.sqlPage = sqlPage;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        // 前端传值page从1开始 ,但sql是从0开始
        if (page == null || page <= 0) {
            this.page = 0L;
            this.sqlPage = 0L;
        } else {
            this.page = page;
            this.sqlPage = (page - 1) * limit;
        }
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = StringUtils.isBlank(order) ? "asc" : order;
    }
}
