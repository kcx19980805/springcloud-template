package com.kcx.common.utils.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 实体转换类，用于实体类型转换
 */
@Slf4j
public class BeanConvertUtils {
    /**
     * 将数据转为List实体
     * @param resultData List实体数据
     * @param entity 实体类
     */
    public static <T> List<T> dataToListEntity(Object resultData, Class<T> entity){
        return JSONObject.parseArray(JSONArray.toJSONString(resultData),entity);
    }

    /**
     * 将数据转为List实体
     * @param resultData List实体数据
     * @param entity 实体类
     */
    public static <T> List<T> dataToListEntity(String resultData, Class<T> entity){
        return JSONObject.parseArray(resultData,entity);
    }

    /**
     * 将数据转为单个实体
     * @param resultData 单个实体数据
     * @param entity 实体类
     */
    public static <T> T dataToEntity(Object resultData, Class<T> entity){
        return JSON.parseObject(JSONObject.toJSONString(resultData), entity);
    }

    /**
     * 将数据转为单个实体
     * @param resultData 单个实体数据
     * @param entity 实体类
     */
    public static <T> T dataToEntity(String resultData, Class<T> entity){
        return JSON.parseObject(resultData, entity);
    }

    /**
     * 小写驼峰命名转下划线，对象中为null的值被忽略
     * 主要用于entity转为第三方的接口数据时，第三方的属性是下划线方式命名的
     * @param entity 实体类
     */
    public static JSONObject lowercaseHumpToUnderline(Object entity){
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        String json = JSON.toJSONString(entity, config);
        return  JSONObject.parseObject(json);
    }

    /**
     * 将字符串驼峰转为下划线
     * @param str 字符串
     * @return
     */
    public static String humpToUnderline(String str) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotEmpty(str)) {
            Matcher matcher = Pattern.compile("[A-Z]").matcher(str);
            while (matcher.find()) {
                matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
            }
            matcher.appendTail(sb);
        }
        return sb.toString();
    }


    /**
     * 下划线转小写驼峰命名
     * 主要用于将第三方收到的数据转为entity
     * @param resultData 返回的实体数据
     * @param entity 实体类
     */
    public static <T> T underlineToLowercaseHump(Object resultData,Class<T> entity) {
        convert(resultData);
        return JSONObject.parseObject(JSONObject.toJSONString(resultData),entity);
    }


    private static void convert(Object resultData) {
        if (resultData instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) resultData;
            for (Object json : jsonArray) {
                convert(json);
            }
        } else if (resultData instanceof JSONObject) {
            JSONObject json = (JSONObject) resultData;
            Set<String> keySet = json.keySet();
            String[] keyArray = keySet.toArray(new String[keySet.size()]);
            for (String key : keyArray) {
                Object value = json.get(key);
                String[] keyStrs = key.split("_");
                if (keyStrs.length > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < keyStrs.length; i++) {
                        String keyStr = keyStrs[i];
                        if (!keyStr.isEmpty()) {
                            if (i == 0) {
                                sb.append(keyStr);
                            } else {
                                int c = keyStr.charAt(0);
                                if (c >= 97 && c <= 122) {
                                    int v = c - 32;
                                    sb.append((char) v);
                                    if (keyStr.length() > 1) {
                                        sb.append(keyStr.substring(1));
                                    }
                                } else {
                                    sb.append(keyStr);
                                }
                            }
                        }
                    }
                    json.remove(key);
                    json.put(sb.toString(), value);
                }
                convert(value);
            }
        }
    }

    /**
     * 单个实体转为map，key为属性名称，value对属性值
     * @param entity 实体类
     * @return
     */
    public static Map<String, Object> entityToMap(Object entity) {
        Map<String, Object> map = new HashMap<>();
        if (entity == null) {
            return map;
        }
        Class clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(entity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * map转为单个实体
     * @param entity 实体类
     * @param resultData 返回的实体数据
     */
    public static <T> T mapToEntity(Class<T> entity, Map<String, Object> resultData) {
        if (resultData == null) {
            return null;
        }
        T obj = null;
        try {
            obj = entity.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, resultData.get(field.getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }


    /**
     * 将Map<String, Object>转换为 key=value&key=value 形式的url请求参数
     */
    public static String mapToUrlParams(Map<String, Object> map) {
        StringBuilder str = new StringBuilder();
        Set<String> set = map.keySet();
        for (String key : set) {
            str.append(key).append("=").append(map.get(key).toString()).append("&");
        }
        return str.substring(0, str.lastIndexOf("&"));
    }
}
