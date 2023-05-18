package com.kcx.common.utils.mongo;

import com.kcx.common.utils.bean.BeanConvertUtils;
import com.kcx.common.utils.page.responseVo.ResPageBaseVO;
import com.kcx.common.utils.reflect.ReflectUtils;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MongoDB工具类
 */
@Slf4j
public class MongoDBUtils {
    /*--------------------------------------------新增------------------------------------------------------------*/

    /**
     * 插入单个文档,文档不存在自动创建，自动生成id
     *
     * @param entity        要插入的文档实体类
     * @param mongoTemplate ioc容器中的mongoTemplate
     * @param <T>
     * @return true成功false失败
     */
    public static <T> boolean insert(T entity, MongoTemplate mongoTemplate) {
        Object t = mongoTemplate.insert(entity);
        return t != null;
    }

    /**
     * 批量添加多个文档,文档不存在自动创建，自动生成id
     *
     * @param entityList    要插入的文档实体类
     * @param mongoTemplate ioc容器中的mongoTemplate
     * @param <T>
     * @return 添加成功的数量
     */
    public static <T> int insertBatch(List<T> entityList, MongoTemplate mongoTemplate) {
        Collection<T> collection = mongoTemplate.insertAll(entityList);
        int successCount = collection.size();
        int size = entityList.size();
        if (size != successCount) {
            log.warn("mongo批量新增完成但有异常，添加的文档数：{}，成功的文档数：{}", size, successCount);
        }
        return size;
    }

    /*--------------------------------------------查询------------------------------------------------------------*/

    /**
     * 根据id查询单个文档
     *
     * @param id            文档自动生成的id值
     * @param tClass        文档实体类
     * @param mongoTemplate ioc容器中的mongoTemplate
     * @param <T>
     * @return 文档实体
     */
    public static <T> T selectById(String id, Class<T> tClass, MongoTemplate mongoTemplate) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("_id").is(id);
        query.addCriteria(criteria);
        List<T> list = mongoTemplate.find(query, tClass);
        if (null != list) {
            if (list.size() > 1) {
                log.warn("MongoDB查询单个文档,但有多个id相同的文档");
            }
            return (T) list.get(0);
        }
        return null;
    }

    /**
     * 根据id批量查询文档
     *
     * @param idList        文档id
     * @param tClass        文档实体类
     * @param mongoTemplate ioc容器中的mongoTemplate
     * @param <T>
     * @return
     */
    public static <T> List<T> selectBatchById(List<String> idList, Class<T> tClass, MongoTemplate mongoTemplate) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("_id").in(idList);
        query.addCriteria(criteria);
        List<T> list = mongoTemplate.find(query, tClass);
        return list;
    }

    /**
     * 查询文档总数
     *
     * @param entity        要搜索的字段条件，赋值到文档实体类，and连接
     * @param mongoTemplate ioc容器中的mongoTemplate
     * @param <T>
     * @return 总数
     */
    public static <T> long selectCount(T entity, MongoTemplate mongoTemplate) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        HashMap<String, String> hashMap = ReflectUtils.getFiledValue(entity);
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            criteria.and(entry.getKey()).is(entry.getValue());
        }
        query.addCriteria(criteria);
        return mongoTemplate.count(query, entity.getClass());
    }

    /**
     * 查询文档列表
     *
     * @param entity        要搜索的字段条件，赋值到文档实体类，and连接
     * @param skip          分页起始位置 可以为null，注意是limit*(page-1)
     * @param limit         分页条数 可以为null
     * @param resClass      返回的实体类
     * @param mongoTemplate ioc容器中的mongoTemplate
     * @param <T>
     * @return 文档列表
     */
    public static <T> List<? extends ResPageBaseVO> selectList(T entity, Long skip, Integer limit, Class<? extends ResPageBaseVO> resClass, MongoTemplate mongoTemplate) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        HashMap<String, String> hashMap = ReflectUtils.getFiledValue(entity);
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            criteria.and(entry.getKey()).is(entry.getValue());
        }
        query.addCriteria(criteria);
        if (null != skip) {
            query.skip(skip);
        }
        if (null != limit) {
            query.limit(limit);
        }
        List<?> list = mongoTemplate.find(query, entity.getClass());
        return BeanConvertUtils.dataToListEntity(list, resClass);
    }


    /*--------------------------------------------修改------------------------------------------------------------*/

    /**
     * 根据id修改单个文档
     *
     * @param id            文档自动生成的id
     * @param entity        要修改的字段值，赋值到文档实体类
     * @param mongoTemplate ioc容器中的mongoTemplate
     * @param <T>
     * @return 受影响行数
     */
    public static <T> int updateById(String id, T entity, MongoTemplate mongoTemplate) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("_id").is(id);
        Update update = new Update();
        HashMap<String, String> hashMap = ReflectUtils.getFiledValue(entity);
        //如果存在id，删除
        hashMap.remove("id");
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            update.set(entry.getKey(), entry.getValue());
        }
        query.addCriteria(criteria);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, entity.getClass());
        return (int) updateResult.getModifiedCount();
    }

    /**
     * 根据id批量修改文档
     *
     * @param idList        文档自动生成的id
     * @param entity        要修改的字段值，赋值到文档实体类
     * @param mongoTemplate ioc容器中的mongoTemplate
     * @param <T>
     * @return 受影响行数
     */
    public static <T> int updateBatchById(List<String> idList, T entity, MongoTemplate mongoTemplate) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("_id").in(idList);
        Update update = new Update();
        HashMap<String, String> hashMap = ReflectUtils.getFiledValue(entity);
        //如果存在id，删除
        hashMap.remove("id");
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            update.set(entry.getKey(), entry.getValue());
        }
        query.addCriteria(criteria);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, entity.getClass());
        int successCount = (int) updateResult.getModifiedCount();
        int size = idList.size();
        if (size != successCount) {
            log.warn("mongo批量修改完成但有异常，修改的文档数：{}，成功的文档数：{}", size, successCount);
        }
        return successCount;
    }

    /**
     * 根据条件批量修改文档
     *
     * @param filterFiled   条件字段，and连接，赋值到文档实体类
     * @param updateFiled   要修改的字段，赋值到文档实体类
     * @param mongoTemplate ioc容器中的mongoTemplate
     * @param <T>
     * @return 受影响行数
     */
    public static <T> int updateBatchByQuery(T filterFiled, T updateFiled, MongoTemplate mongoTemplate) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        HashMap<String, String> filterMap = ReflectUtils.getFiledValue(filterFiled);
        for (Map.Entry<String, String> entry : filterMap.entrySet()) {
            criteria.and(entry.getKey()).is(entry.getValue());
        }
        Update update = new Update();
        HashMap<String, String> updateMap = ReflectUtils.getFiledValue(updateFiled);
        //如果存在id，删除
        updateMap.remove("id");
        for (Map.Entry<String, String> entry : updateMap.entrySet()) {
            update.set(entry.getKey(), entry.getValue());
        }
        query.addCriteria(criteria);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, updateFiled.getClass());
        return (int) updateResult.getModifiedCount();
    }


    /*--------------------------------------------删除------------------------------------------------------------*/

    /**
     * 根据id删除单个文档
     *
     * @param id            文档自动生成的id
     * @param tClass        文档实体类
     * @param mongoTemplate ioc容器中的mongoTemplate
     * @param <T>
     * @return 受影响行数
     */
    public static <T> int deleteById(String id, Class<T> tClass, MongoTemplate mongoTemplate) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("_id").is(id);
        query.addCriteria(criteria);
        DeleteResult deleteResult = mongoTemplate.remove(query, tClass);
        return (int) deleteResult.getDeletedCount();
    }

    /**
     * 根据id批量删除文档
     *
     * @param idList        文档自动生成的id
     * @param tClass        文档实体类
     * @param mongoTemplate ioc容器中的mongoTemplate
     * @param <T>
     * @return 受影响行数
     */
    public static <T> int deleteBatchById(List<String> idList, Class<T> tClass, MongoTemplate mongoTemplate) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("_id").in(idList);
        query.addCriteria(criteria);
        DeleteResult deleteResult = mongoTemplate.remove(query, tClass);
        int successCount = (int) deleteResult.getDeletedCount();
        int size = idList.size();
        if (size != successCount) {
            log.warn("mongo批量删除完成但有异常，删除的文档数：{}，成功的文档数：{}", size, successCount);
        }
        return successCount;
    }

    /**
     * 根据条件批量删除文档
     *
     * @param entity        条件字段，and连接，赋值到文档实体类
     * @param mongoTemplate ioc容器中的mongoTemplate
     * @param <T>
     * @return 受影响行数
     */
    public static <T> int delete(T entity, MongoTemplate mongoTemplate) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        HashMap<String, String> hashMap = ReflectUtils.getFiledValue(entity);
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            criteria.and(entry.getKey()).is(entry.getValue());
        }
        query.addCriteria(criteria);
        DeleteResult deleteResult = mongoTemplate.remove(query, entity.getClass());
        return (int) deleteResult.getDeletedCount();
    }


}
