package com.kcx.common.utils.id;

import cn.hutool.core.lang.UUID;

/**
 * UUID生成分布式全局唯一id
 * 标准形式包含32个16进制数字,性能非常高，本地生成没有网络消耗。
 * 如果只考虑唯一性ok，但入数据库性能比较差，它是无序的，mysql主键太长会增加索引空间。
 */
public class UuIdUtils
{
    /**
     * 获取随机UUID
     *
     * @return 随机UUID
     */
    public static String randomUUID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUUID()
    {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 随机UUID
     */
    public static String fastUUID()
    {
        return UUID.fastUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String fastSimpleUUID()
    {
        return UUID.fastUUID().toString(true);
    }

    public static void main(String[] args) {

        System.out.println(randomUUID());
    }
}
