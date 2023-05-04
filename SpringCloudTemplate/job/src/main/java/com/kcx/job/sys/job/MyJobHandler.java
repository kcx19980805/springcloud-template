package com.kcx.job.sys.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyJobHandler {
    private Logger log = LoggerFactory.getLogger(MyJobHandler.class);

    /**
     * 1.普通任务
     * @param param
     * @return
     */
    @XxlJob(value = "kcxJobHandler",init = "init",destroy = "destroy")
    public ReturnT<String> execute(String param){
        log.info("kcx-executor执行器下的kcxJobHandler任务触发成功");
        String jobParam = XxlJobHelper.getJobParam();
        if(!StringUtils.isEmpty(jobParam)){
            log.info("页面传递参数为："+jobParam);
            XxlJobHelper.handleSuccess();
        }else{
            XxlJobHelper.log(new RuntimeException("页面没有传入参数"));
            XxlJobHelper.handleFail();
        }
        return ReturnT.SUCCESS;
    }


    /**
     * 2、分片广播任务
     */
    @XxlJob("shardingJobHandler")
    public void shardingJobHandler() throws Exception {

        // 当前分片数，从0开始，即执行器的序号
        int shardIndex = XxlJobHelper.getShardIndex();
        //总分片数，执行器集群总机器数量
        int shardTotal = XxlJobHelper.getShardTotal();
        log.info("分片参数：当前分片序号 = {}, 总分片数 = {}", shardIndex, shardTotal);
        XxlJobHelper.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardIndex, shardTotal);
        List<Integer> allUserIds = getAllUserIds();
        allUserIds.forEach(obj -> {
            if (obj % shardTotal == shardIndex) {
                log.info("第 {} 片, 命中分片开始处理用户id={}",shardIndex,obj);
            }
        });
    }

    private List<Integer> getAllUserIds() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ids.add(i);
        }
        return ids;
    }

    private void init(){
        log.info("kcxJobHandler 初始化");
    }

    private void destroy(){
        log.info("kcxJobHandler 销毁了");
    }

}
