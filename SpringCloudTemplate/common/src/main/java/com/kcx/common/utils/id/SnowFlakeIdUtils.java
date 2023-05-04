package com.kcx.common.utils.id;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;

import javax.annotation.PostConstruct;

/**
 * 雪花算法生成分布式唯一ID
 * 1.twitter的SnowFlake生成ID能够安找时间有序生成
 * 2.SnowFlake算法生成id的结果是一个64bit大小的整数，为一个Long型（转换成字符串长度最多为19）。
 * 3.分布式系统内不会产生ID碰撞（由datacenter和workerId作区分）并且效率高。
 */
public class SnowFlakeIdUtils {
    private static long workerId = 0;
    private static long  datacenterId = 1;
    private static Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);

    /**
     * 依赖注入完成后执行初始化的方法
     */
    @PostConstruct
    public void init() {
        try {
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
        } catch (Exception e) {
            e.printStackTrace();
            workerId = NetUtil.getLocalhostStr().hashCode();
        }
    }

    /**
     * 生成雪花算法
     */
    public static synchronized long getSnowflakeId() {
        return snowflake.nextId();
    }

    /**
     * 生成雪花算法
     * @param workerId 终端id
     * @param datacenterId 数据中心id
     * @return
     */
    public static synchronized long getSnowflakeId(long workerId, long datacenterId) {
        Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);
        return snowflake.nextId();
    }

    public static void main(String[] args) {
        System.out.println(new SnowFlakeIdUtils().getSnowflakeId());
    }

}
