package com.kcx.common.utils.sql;

import com.kcx.common.utils.bean.BeanConvertUtils;

import java.lang.reflect.Field;

/**
 * sql语句工具类
 */
public class SqlUtils {

    /**
     * 将类中的字段拼接为sql语句查询字符串
     * 不要将这个结果用变量放入xml，无法解析，用main方法调用后把字符串复制到xml里
     * @param clazz 响应到接口的实体类
     * @return 按,号拼接的字段
     */
    public static String getSelectFieldsString(Class clazz){
        Field[] declaredFields = clazz.getDeclaredFields();
        StringBuffer stringBuffer = new StringBuffer();
        for (Field field : declaredFields) {
            stringBuffer.append(field.getName());
            stringBuffer.append(",");
        }
        return BeanConvertUtils.humpToUnderline(stringBuffer.substring(0,stringBuffer.length()-1));

    }


}
