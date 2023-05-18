package com.kcx.common.utils.reflect;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 反射工具类
 */
@Slf4j
public class ReflectUtils {

    /**
     * 通过反射将实体类的字段和值取出，忽略空值
     * @param entity 实体类
     * @return key为字段名称，value为字段值
     */
    public static <T> HashMap<String, String> getFiledValue(T entity) {
        //顺序插入
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        Class<?> aClass = entity.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                //属性名称
                String name = field.getName();
                //属性值
                String value = String.valueOf(field.get(entity));
                if (!"null".equals(value) && !"".equals(value)) {
                    linkedHashMap.put(name, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return linkedHashMap;
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     * @param clazz 类
     * @param index 参数位置索引
     * @return 参数类型
     */
    public static Class getClassGenericityType(final Class clazz, final int index)
    {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType))
        {
            log.warn("{}的父类没有参数类型",clazz.getSimpleName());
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0)
        {
            log.warn("参数索引位置：{}，但是{}的参数长度只有{}",index,clazz.getSimpleName(),params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class))
        {
            log.warn("未在父类{}泛型参数上设置实际类",clazz.getSimpleName());
            return Object.class;
        }
        return (Class) params[index];
    }
}
