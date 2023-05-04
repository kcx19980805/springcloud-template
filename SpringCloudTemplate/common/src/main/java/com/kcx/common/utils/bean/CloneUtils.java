package com.kcx.common.utils.bean;

import java.io.*;

/**
 * 克隆对象
 */
public class CloneUtils {

    //浅拷贝，对对象中的基本类型复制值，对象中的引用类型传递原对象的引用，自行实现Cloneable接口，重写clone方法，调用super.clone()实现

    /**
     * 深拷贝，使用序列化的方式将对象和对象引用全部复制一份，申请新内存空间
     * @param obj 原对象
     * @param <T>
     * @return 复制的一模一样的新对象
     */
    public static <T extends Serializable> T deepCopy(T obj) {
        T cloneObj = null;
        try {
            //写入字节流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream obs = new ObjectOutputStream(out);
            obs.writeObject(obj);
            obs.close();
            //分配内存，写入原始对象，生成新对象
            ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(ios);
            //返回生成的新对象
            cloneObj = (T) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cloneObj;
    }

}
