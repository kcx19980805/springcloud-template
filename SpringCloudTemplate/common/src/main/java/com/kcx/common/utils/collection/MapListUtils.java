package com.kcx.common.utils.collection;

import java.util.*;

/**
 * Map操作工具类
 */
public class MapListUtils {


    /**
     * 合并两个map
     */
    public static Map<String, Object> addMap(Map<String, Object> target, Map<String, Object> map) {
        if (target == null || target.size() == 0) {
            return map;
        }
        target.forEach((k, v) -> {
            map.put(k, v);
        });
        return map;
    }

}
