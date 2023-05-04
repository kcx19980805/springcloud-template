package com.kcx.common.utils.page;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 分页类
 */
public class PageUtils {

    /**
     * 组装并编号分页数据
     * @param getTotalMethod 查询总数的方法
     * @param getListMethod 根据分页查询列表的方法
     * @param req 请求的实体类，继承ReqPageBaseVO
     * @return 响应的分页数据，包括total，data
     */
    public static ResPageDataVO autoPageData(Supplier<Integer> getTotalMethod,Supplier<List<? extends ResPageBaseVO>> getListMethod,ReqPageBaseVO req){
        Integer total = getTotalMethod.get();
        List<? extends ResPageBaseVO> entityList = new ArrayList<>();
        if (total > 0) {
            entityList = getListMethod.get();
            //每页大小
            int limit = req.getLimit();
            if (entityList.size() > 0  && limit > 0) {
                //当前页码 从0开始
                int currPage = req.getPage()-1;
                //排序 asc升序 desc降序
                String order = req.getOrder();
                for (int i = 0; i < entityList.size(); i++) {
                    //自动生成前端序号
                    entityList.get(i).setNumber("asc".equalsIgnoreCase(order) ? limit * currPage + i + 1 : total - (limit * currPage) - i);
                }
            }
        }
        return new ResPageDataVO(entityList, total);
    }

    /**
     * 手动分页数据
     * @param data 全部数据
     * @param limit 每页数据条数
     * @param pageNo 页码从0开始
     * @param <T> 实体类
     * @return 分页后的数据
     */
    public static <T> List<T> handPageData(List<T> data, int limit, int pageNo){
        //起始截取数据位置
        int startNum = (pageNo)* limit+1 ;
        //参数不合法
        if(pageNo < 0 || limit<=0){
            return new ArrayList<>();
        }
        List<T> res = new ArrayList<>();
        int rum = data.size() - startNum;
        //正好是最后一个
        if(rum == 0){
            int index = data.size() -1;
            res.add(data.get(index));
        }else if(rum > 0){
            int end=0;
            //剩下的数据够1页，返回整页的数据
            if(rum / limit >= 1){
                end=startNum + limit;
            }//不够一页，返回剩下数据
            else if(rum / limit == 0){
                end=data.size();
            }
            for(int i=startNum;i<end;i++){
                res.add(data.get(i-1));
            }
        }
        return res;
    }
}
