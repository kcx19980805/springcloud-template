package com.kcx.common.utils.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kcx.common.exception.CustomException;
import com.kcx.common.utils.bean.BeanConvertUtils;
import com.kcx.common.utils.elasticsearch.requestVo.ReqElasticsearchVo;
import com.kcx.common.utils.elasticsearch.responseVo.ResElasticsearchVo;
import com.kcx.common.utils.page.responseVo.ResPageDataVO;
import com.kcx.common.utils.reflect.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.IOException;
import java.util.*;

/**
 * Elasticsearch工具类
 */
@Slf4j
public class ElasticsearchUtils {

    public static final String docId = "id";


    /*--------------------------------------------新增------------------------------------------------------------*/

    /**
     * 添加单个文档，如果索引不存在则自动创建,如果有id字段则用id，否则自动生成，如果id已存在则是修改
     * 如果没有事先建立好mapping，建议使用jpa自带的save，否则会自动判断类型生成mapping
     *
     * @param entity              文档实体类
     * @param refresh             是否立即刷新缓存，如果数据量大则不建议，数据量小可以
     * @param restHighLevelClient ioc容器中的restHighLevelClient
     * @param <T>
     * @return 返回自动生成的id或指定id
     */
    public static <T> String insert(T entity, boolean refresh, RestHighLevelClient restHighLevelClient) {
        IndexRequest request = new IndexRequest();
        request.index(getIndexName(entity.getClass()));
        JSONObject json = JSONObject.parseObject(JSONArray.toJSONString(entity));
        String id = json.getString(docId);
        if (id != null) {
            request.id(id);
        }
        json.remove(docId);
        request.source(JSONObject.toJSONString(entity), XContentType.JSON);
        if (refresh) {
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        }
        IndexResponse response = null;
        try {
            response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.getId();
    }

    /**
     * 批量添加多个文档，如果索引不存在则自动创建,如果有id字段则用id，否则自动生成，如果id已存在则是修改
     * 如果没有事先建立好mapping，建议使用jpa自带的save，否则会自动判断类型生成mapping
     *
     * @param entityList          文档实体类
     * @param refresh             是否立即刷新缓存，如果数据量大则不建议，数据量小可以
     * @param restHighLevelClient ioc容器中的restHighLevelClient
     * @param <T>
     * @return 添加成功的数量
     */
    public static <T> int insertBatch(List<T> entityList, boolean refresh, RestHighLevelClient restHighLevelClient) {
        BulkRequest bulkRequest = new BulkRequest();
        if(null != entityList && entityList.size()>0){
            String indexName = getIndexName(entityList.get(0).getClass());
            for (T entity : entityList) {
                IndexRequest request = new IndexRequest();
                request.index(indexName);
                JSONObject json = JSONObject.parseObject(JSONArray.toJSONString(entity));
                String id = json.getString(docId);
                if (id != null) {
                    request.id(id);
                }
                json.remove(docId);
                request.source(JSONObject.toJSONString(entity), XContentType.JSON);
                bulkRequest.add(request);
            }
        }
        if (refresh) {
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        }
        BulkResponse response = null;
        try {
            response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("es批量新增完成，耗时：{}", response.getTook());
        int successCount = response.getItems().length;
        int size = entityList.size();
        if (size != successCount) {
            log.warn("es批量新增完成但有异常，添加的文档数：{}，成功的文档数：{}", size, successCount);
        }
        return successCount;
    }


    /*--------------------------------------------查询------------------------------------------------------------*/

    /**
     * 根据id查询单个文档
     *
     * @param id                  id
     * @param entity              文档实体类
     * @param restHighLevelClient ioc容器中的restHighLevelClient
     * @param <T>
     * @return
     */
    public static <T> T selectById(String id, Class<T> entity, RestHighLevelClient restHighLevelClient) {
        GetRequest request = new GetRequest();
        request.index(getIndexName(entity)).id(id);
        GetResponse response = null;
        try {
            response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON.parseObject(response.getSourceAsString(), entity);
    }

    /**
     * 简单查询列表
     *
     * @param entity              条件字段，and连接，赋值到实体类
     * @param from                分页起始位置 可以为null，注意是limit*(page-1)
     * @param size                分页条数 可以为null
     * @param restHighLevelClient ioc容器中的restHighLevelClient
     * @param <T>
     * @return 数据列表和总数，如果没有分页，则总数=列表数
     */
    public static <T> ResElasticsearchVo selectList(T entity, Integer from, Integer size, RestHighLevelClient restHighLevelClient) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(getIndexName(entity.getClass()));
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //取消es最大查询10000限制
        builder.trackTotalHits(true);
        //查询条件
        HashMap<String, String> hashMap = ReflectUtils.getFiledValue(entity);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            //must是必须，should是或
            boolQueryBuilder.must(QueryBuilders.matchQuery(entry.getKey(), entry.getValue()));
        }
        builder.query(boolQueryBuilder);
        //分页
        builder.from(from);
        builder.size(size);
        SearchResponse response = executeSearch(searchRequest, builder, restHighLevelClient);
        //命中数据
        SearchHits hits = response.getHits();
        //id和其他字段
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        //数据总数
        long total = hits.getTotalHits().value;
        for (SearchHit hit : hits) {
            data.put(hit.getId(), hit.getSourceAsString());
        }
        ResElasticsearchVo resElasticsearchVo = new ResElasticsearchVo();
        resElasticsearchVo.setData(data);
        resElasticsearchVo.setTotal(total);
        return resElasticsearchVo;
    }

    /**
     * 通用复杂查询列表
     *
     * @param classT              文档实体类
     * @param req                 查询条件
     * @param restHighLevelClient ioc容器中的restHighLevelClient
     * @return 数据列表和总数，如果没有分页，则总数=列表数
     */
    public static ResElasticsearchVo selectFull(Class<?> classT, ReqElasticsearchVo req, RestHighLevelClient restHighLevelClient) {
        SearchRequest request = new SearchRequest();
        request.indices(getIndexName(classT));
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //取消es最大查询10000限制
        builder.trackTotalHits(true);
        //构建查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        builder.query(buildQuery(boolQueryBuilder, req));
        //所有的查询条件都放在bool查询中
        //要去重的字段
        if (!StringUtils.isEmpty(req.getCollapseField())) {
            builder.collapse(new CollapseBuilder(req.getCollapseField()));
            //单独聚合，统计去重后的总数，而非命中总数
            CardinalityAggregationBuilder totalAggregation = AggregationBuilders.cardinality("total").field(req.getCollapseField());
            builder.aggregation(totalAggregation);
        }
        //要排序字段
        Map<String, SortOrder> sortMap = req.getSortMap();
        if (sortMap != null) {
            Set<String> keys = sortMap.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                SortOrder sort = sortMap.get(key);
                builder.sort(key, sort);
            }
        }
        //要高亮的字段
        String highlightFiled = req.getHighlightFiled();
        if (highlightFiled != null) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.preTags("<font color='red'>");
            highlightBuilder.postTags("</font>");
            highlightBuilder.field(highlightFiled);
            builder.highlighter(highlightBuilder);
        }
        //控制返回的字段
        List<String> includeFields = req.getIncludeFields();
        List<String> excludeFields = req.getExcludeFields();
        String[] includes = null;
        if (includeFields != null) {
            //包含的字段
            includes = new String[includeFields.size()];
            includes = includeFields.toArray(includes);
        }
        String[] excludes = null;
        if (excludeFields != null) {
            //排除的字段
            excludes = new String[excludeFields.size()];
            excludes = excludeFields.toArray(excludes);
        }
        builder.fetchSource(includes, excludes);
        //分页
        int pageNo = req.getPageNo();
        int pageSize = req.getPageSize();
        builder.from((pageNo - 1) * pageSize);
        builder.size(pageSize);
        //处理查询结果
        SearchResponse response = executeSearch(request, builder, restHighLevelClient);
        long total;
        if (!StringUtils.isEmpty(req.getCollapseField())) {
            //统计去重后的总数
            Aggregations aggregations = response.getAggregations();
            ParsedCardinality totalCount = aggregations.get("total");
            total = totalCount.getValue();
        } else {
            total = response.getHits().getTotalHits().value;
        }
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            String id = hit.getId();
            String sourceAsString = hit.getSourceAsString();
            data.put(id, sourceAsString);
        }
        ResElasticsearchVo resElasticsearchVo = new ResElasticsearchVo();
        resElasticsearchVo.setTotal(total);
        resElasticsearchVo.setData(data);
        return resElasticsearchVo;
    }

    /**
     * 模糊查询列表,es不同版本，高版本可能会失效，fuzzy是旧版的，新版已经是wildcard
     *
     * @param classT              文档实体类
     * @param restHighLevelClient ioc容器中的restHighLevelClient
     * @param entity              模糊匹配的字段，设置到对应实体类属性值中
     * @return 数据列表和总数，如果没有分页，则总数=列表数
     */
    public static <T> ResElasticsearchVo selectFuzziness(Class<?> classT, RestHighLevelClient restHighLevelClient, T entity) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(getIndexName(classT));
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //取消es最大查询10000限制
        builder.trackTotalHits(true);
        HashMap<String, String> hashMap = ReflectUtils.getFiledValue(entity);
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            //模糊查询，允许一个字符误差
            builder.query(QueryBuilders.fuzzyQuery(entry.getKey(), entry.getValue()).fuzziness(Fuzziness.ONE));
        }
        SearchResponse response = executeSearch(searchRequest, builder, restHighLevelClient);
        //命中数据
        SearchHits hits = response.getHits();
        //id和其他字段
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        //数据总数
        long total = hits.getTotalHits().value;
        for (SearchHit hit : hits) {
            data.put(hit.getId(), hit.getSourceAsString());
        }
        ResElasticsearchVo resElasticsearchVo = new ResElasticsearchVo();
        resElasticsearchVo.setData(data);
        resElasticsearchVo.setTotal(total);
        return resElasticsearchVo;
    }

    /**
     * 聚合查询
     *
     * @param classT              文档实体类
     * @param restHighLevelClient ioc容器中的restHighLevelClient
     * @param fields              需要统计的字段，key为字段名称，value为:max统计最大值,min统计最小值,count统计不为null的数量
     * @return key为字段，value为统计的值
     */
    public static Map<String, Double> selectAggregation(Class<?> classT, RestHighLevelClient restHighLevelClient, Map<String, String> fields) {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(getIndexName(classT));
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //取消es最大查询10000限制
        builder.trackTotalHits(true);
        //字段别名等于字段名称，统计字段最大值和最小值
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            builder.query(QueryBuilders.fuzzyQuery(entry.getKey(), entry.getValue()).fuzziness(Fuzziness.ONE));
            if ("max".equals(entry.getValue())) {
                builder.aggregation(AggregationBuilders.max(entry.getKey()).field(entry.getKey()));
            }
            if ("min".equals(entry.getValue())) {
                builder.aggregation(AggregationBuilders.min(entry.getKey()).field(entry.getKey()));
            }
            if ("count".equals(entry.getValue())) {
                builder.aggregation(AggregationBuilders.count(entry.getKey()).field(entry.getKey()));
            }
        }
        SearchResponse response = executeSearch(searchRequest, builder, restHighLevelClient);
        final Map<String, Aggregation> stringAggregationMap = response.getAggregations().asMap();
        HashMap<String, Double> resultMap = new HashMap<>();
        stringAggregationMap.forEach((field, aggregation) -> {
            if ("max".equals(fields.get(field))) {
                ParsedMax parsedMax = (ParsedMax) aggregation;
                resultMap.put(field, parsedMax.getValue());
            }
            if ("min".equals(fields.get(field))) {
                ParsedMin parsedMin = (ParsedMin) aggregation;
                resultMap.put(field, parsedMin.getValue());
            }
            if ("count".equals(fields.get(field))) {
                ParsedValueCount parsedValueCount = (ParsedValueCount) aggregation;
                resultMap.put(field, (double) parsedValueCount.getValue());
            }
        });
        return resultMap;
    }


    /*--------------------------------------------修改------------------------------------------------------------*/


    /**
     * 根据id修改单个文档
     *
     * @param entity              文档实体类，修改的字段值设置进去
     * @param refresh             是否立即刷新缓存，如果数据量大则不建议，数据量小可以
     * @param restHighLevelClient ioc容器中的restHighLevelClient
     * @param <T>
     * @return 删除成功/失败
     */
    public static <T> boolean updateById(T entity, boolean refresh, RestHighLevelClient restHighLevelClient) {
        //修改数据
        UpdateRequest request = new UpdateRequest();
        JSONObject json = JSONObject.parseObject(JSONArray.toJSONString(entity));
        String id = json.getString(docId);
        if (id == null) {
            throw new CustomException("id不能为空");
        }
        json.remove(docId);
        request.index(getIndexName(entity.getClass())).id(id);
        request.doc(json, XContentType.JSON);
        UpdateResponse response = null;
        if (refresh) {
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        }
        try {
            response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.getShardInfo().getFailed() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据id批量更新文档
     *
     * @param idList              文档id
     * @param entity              文档实体类，修改的字段值设置进去
     * @param refresh             是否立即刷新缓存，如果数据量大则不建议，数据量小可以
     * @param restHighLevelClient ioc容器中的restHighLevelClient
     * @param <T>
     * @return 修改成功的数量
     */
    public static <T> long updateBatchById(List<String> idList, T entity, boolean refresh, RestHighLevelClient restHighLevelClient) {
        String indexName = getIndexName(entity.getClass());
        BulkRequest bulkRequest = new BulkRequest();
        JSONObject json = JSONObject.parseObject(JSONArray.toJSONString(entity));
        json.remove(docId);
        if (null != idList) {
            for (String id : idList) {
                bulkRequest.add(new UpdateRequest().index(indexName).id(id).doc(json, XContentType.JSON));
            }
        }
        if (refresh) {
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        }
        BulkResponse response = null;
        try {
            response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("es批量更新完成，耗时：{}", response.getTook());
        int successCount = response.getItems().length;
        int size = idList.size();
        if (size != successCount) {
            log.warn("es批量更新完成但有异常，更新的文档数：{}，成功的文档数：{}", size, successCount);
        }
        return successCount;
    }

    /**
     * 根据条件批量修改文档
     *
     * @param req                 查询条件，注意高亮，排序，去重，指定返回字段等不需要
     * @param entity              文档实体类，修改的字段值设置进去
     * @param refresh             是否立即刷新缓存，如果数据量大则不建议，数据量小可以
     * @param restHighLevelClient ioc容器中的restHighLevelClient
     * @return 修改成功的数量
     */
    public static <T> long updateBatchByQuery(ReqElasticsearchVo req, T entity, boolean refresh, RestHighLevelClient restHighLevelClient) {
        UpdateByQueryRequest request = new UpdateByQueryRequest(getIndexName(entity.getClass()));
        //版本冲突 继续
        request.setConflicts("proceed");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        request.setQuery(buildQuery(boolQueryBuilder, req));
        if (refresh) {
            request.setRefresh(true);
        }
        //拼接脚本
        HashMap<String, String> hashMap = ReflectUtils.getFiledValue(entity);
        hashMap.remove(docId);
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            stringBuffer.append("ctx._source['" + entry.getKey() + "']='" + entry.getValue() + "';");
        }
        String script = stringBuffer.toString();
        log.info("es批量修改脚本：{}", script);
        request.setScript(new Script(ScriptType.INLINE, "painless", script, Collections.emptyMap()));
        BulkByScrollResponse bulkResponse = null;
        try {
            bulkResponse = restHighLevelClient.updateByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("es批量修改完成，耗时：{}", bulkResponse.getTook());
        long updated = bulkResponse.getUpdated();
        return updated;
    }

    /*--------------------------------------------删除------------------------------------------------------------*/

    /**
     * 根据id删除单个文档
     *
     * @param id                  文档id
     * @param classT              文档实体类
     * @param restHighLevelClient ioc容器中的restHighLevelClient
     * @return 删除成功/失败
     */
    public static boolean deleteById(String id,Class<?> classT, RestHighLevelClient restHighLevelClient) {
        DeleteRequest request = new DeleteRequest();
        request.index(getIndexName(classT)).id(id);
        DeleteResponse response = null;
        try {
            response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.getShardInfo().getFailed() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据id批量删除文档
     *
     * @param idList              文档id
     * @param classT              文档实体类
     * @param refresh             是否立即刷新缓存，如果数据量大则不建议，数据量小可以
     * @param restHighLevelClient ioc容器中的restHighLevelClient
     * @return 删除成功的数量
     */
    public static int deleteBatchById(List<String> idList,Class<?> classT, boolean refresh, RestHighLevelClient restHighLevelClient) {
        String indexName = getIndexName(classT);
        BulkRequest bulkRequest = new BulkRequest();
        if (null != idList) {
            for (String id : idList) {
                bulkRequest.add(new DeleteRequest().index(indexName).id(id));
            }
        }
        if (refresh) {
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        }
        BulkResponse response = null;
        try {
            response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("es批量删除完成，耗时：{}", response.getTook());
        int successCount = response.getItems().length;
        int size = idList.size();
        if (size != successCount) {
            log.warn("es批量删除完成但有异常，删除的文档数：{}，成功的文档数：{}", size, successCount);
        }
        return successCount;
    }

    /**
     * 根据条件批量删除文档
     *
     * @param req                 查询条件，注意高亮，排序，去重，指定返回字段等不需要
     * @param classT              文档实体类
     * @param refresh             是否立即刷新缓存，如果数据量大则不建议，数据量小可以
     * @param restHighLevelClient ioc容器中的restHighLevelClient
     * @return 删除成功的数量
     */
    public static long deleteBatchByQuery(ReqElasticsearchVo req,Class<?> classT, boolean refresh, RestHighLevelClient restHighLevelClient) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(getIndexName(classT));
        //版本冲突 继续
        request.setConflicts("proceed");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        request.setQuery(buildQuery(boolQueryBuilder, req));
        if (refresh) {
            request.setRefresh(true);
        }
        BulkByScrollResponse bulkResponse = null;
        try {
            bulkResponse = restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("es批量删除完成，耗时：{}", bulkResponse.getTook());
        long deleted = bulkResponse.getDeleted();
        return deleted;
    }

    /**
     * 构建复杂查询条件
     *
     * @param boolQueryBuilder
     * @param req
     * @return
     */
    public static BoolQueryBuilder buildQuery(BoolQueryBuilder boolQueryBuilder, ReqElasticsearchVo req) {
        //不分词，且连接，字段单值精确匹配
        Map<String, Object> mustTermMap = req.getMustTermMap();
        if (mustTermMap != null) {
            for (Map.Entry<String, Object> entry : mustTermMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = key.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    //nestedBoolQueryBuilder.must(QueryBuilders.termQuery("detailList.car", "1"));
                    nestedBoolQueryBuilder.must(QueryBuilders.termQuery(key, value));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    boolQueryBuilder.must(nestedQueryBuilder);
                } else {
                    boolQueryBuilder.must(QueryBuilders.termQuery(key, value));
                }
            }
        }
        //不分词，或连接，字段单值精确匹配
        Map<String, Object> shouldTermMap = req.getShouldTermMap();
        if (shouldTermMap != null) {
            for (Map.Entry<String, Object> entry : shouldTermMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = key.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    nestedBoolQueryBuilder.must(QueryBuilders.termQuery(key, value));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    boolQueryBuilder.should(nestedQueryBuilder);
                } else {
                    boolQueryBuilder.should(QueryBuilders.termQuery(key, value));
                }
            }
        }

        //不分词,且连接,字段多值精确匹配
        Map<String, List<Object>> mustTermsMap = req.getMustTermsMap();
        if (mustTermsMap != null) {
            for (Map.Entry<String, List<Object>> entry : mustTermsMap.entrySet()) {
                String key = entry.getKey();
                List<Object> value = entry.getValue();
                if (key.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = key.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    nestedBoolQueryBuilder.must(QueryBuilders.termsQuery(key, value));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    boolQueryBuilder.must(nestedQueryBuilder);
                } else {
                    boolQueryBuilder.must(QueryBuilders.termsQuery(key, value));
                }
            }
        }
        //不分词,或连接,字段多值精确匹配
        Map<String, List<Object>> shouldTermsMap = req.getShouldTermsMap();
        if (shouldTermsMap != null) {
            for (Map.Entry<String, List<Object>> entry : shouldTermsMap.entrySet()) {
                String key = entry.getKey();
                List<Object> value = entry.getValue();
                if (key.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = key.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    nestedBoolQueryBuilder.must(QueryBuilders.termsQuery(key, value));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    boolQueryBuilder.should(nestedQueryBuilder);
                } else {
                    boolQueryBuilder.should(QueryBuilders.termsQuery(key, value));
                }
            }
        }

        //不分词,且连接,模糊匹配
        Map<String, String> mustWildcardMap = req.getMustWildcardMap();
        if (mustWildcardMap != null) {
            for (Map.Entry<String, String> entry : mustWildcardMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = key.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    nestedBoolQueryBuilder.must(QueryBuilders.wildcardQuery(key, value));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    boolQueryBuilder.must(nestedQueryBuilder);
                } else {
                    boolQueryBuilder.must(QueryBuilders.wildcardQuery(key, value));
                }
            }
        }
        //不分词,或连接,模糊匹配
        Map<String, String> shouldWildcardMap = req.getShouldWildcardMap();
        if (shouldWildcardMap != null) {
            for (Map.Entry<String, String> entry : shouldWildcardMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = key.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    nestedBoolQueryBuilder.must(QueryBuilders.wildcardQuery(key, value));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    boolQueryBuilder.should(nestedQueryBuilder);
                } else {
                    boolQueryBuilder.should(QueryBuilders.wildcardQuery(key, value));
                }
            }
        }

        //分词，且连接,字段匹配
        Map<String, Object> mustMatchMap = req.getMustMatchMap();
        if (mustMatchMap != null) {
            for (Map.Entry<String, Object> entry : mustMatchMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = key.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    nestedBoolQueryBuilder.must(QueryBuilders.matchQuery(key, value));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    boolQueryBuilder.must(nestedQueryBuilder);
                } else {
                    boolQueryBuilder.must(QueryBuilders.matchQuery(key, value));
                }
            }
        }
        //分词，或连接,字段匹配
        Map<String, Object> shouldMatchMap = req.getShouldMatchMap();
        if (shouldMatchMap != null) {
            for (Map.Entry<String, Object> entry : shouldMatchMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = key.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    nestedBoolQueryBuilder.must(QueryBuilders.matchQuery(key, value));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    boolQueryBuilder.should(nestedQueryBuilder);
                } else {
                    boolQueryBuilder.should(QueryBuilders.matchQuery(key, value));
                }
            }
        }

        //分词,且连接,短语匹配
        Map<String, Object> mustMatchPhraseMap = req.getMustMatchPhraseMap();
        if (mustMatchPhraseMap != null) {
            for (Map.Entry<String, Object> entry : mustMatchPhraseMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = key.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    nestedBoolQueryBuilder.must(QueryBuilders.matchPhraseQuery(key, value));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    boolQueryBuilder.must(nestedQueryBuilder);
                } else {
                    boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(key, value));
                }
            }
        }
        //分词,或连接,短语匹配
        Map<String, Object> shouldMatchPhraseMap = req.getShouldMatchPhraseMap();
        if (shouldMatchPhraseMap != null) {
            for (Map.Entry<String, Object> entry : shouldMatchPhraseMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = key.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    nestedBoolQueryBuilder.must(QueryBuilders.matchPhraseQuery(key, value));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    boolQueryBuilder.should(nestedQueryBuilder);
                } else {
                    boolQueryBuilder.should(QueryBuilders.matchPhraseQuery(key, value));
                }
            }
        }

        //分词,且连接,多字段匹配
        Map<Object, List<String>> mustMultiMatchMap = req.getMustMultiMatchMap();
        if (mustMultiMatchMap != null) {
            for (Map.Entry<Object, List<String>> entry : mustMultiMatchMap.entrySet()) {
                Object key = entry.getKey();
                List<String> value = entry.getValue();
                String[] valueArray = new String[value.size()];
                valueArray = value.toArray(valueArray);
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(key, valueArray));
            }
        }
        //分词,或连接,多字段匹配
        Map<Object, List<String>> shouldMultiMatchMap = req.getShouldMultiMatchMap();
        if (shouldMultiMatchMap != null) {
            for (Map.Entry<Object, List<String>> entry : shouldMultiMatchMap.entrySet()) {
                Object key = entry.getKey();
                List<String> value = entry.getValue();
                String[] valueArray = new String[value.size()];
                valueArray = value.toArray(valueArray);
                boolQueryBuilder.should(QueryBuilders.multiMatchQuery(key, valueArray));
            }
        }

        //不分词,且连接，字段不匹配
        Map<String, Object> mustNotTermMap = req.getMustNotTermMap();
        if (mustNotTermMap != null) {
            for (Map.Entry<String, Object> entry : mustNotTermMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = key.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    nestedBoolQueryBuilder.must(QueryBuilders.termQuery(key, value));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    boolQueryBuilder.mustNot(nestedQueryBuilder);
                } else {
                    boolQueryBuilder.mustNot(QueryBuilders.termQuery(key, value));
                }
            }
        }

        //且连接，必须不为null的字段
        List<String> mustExistsFields = req.getMustExistsFields();
        if (mustExistsFields != null) {
            for (String filed : mustExistsFields) {
                if (filed.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = filed.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    nestedBoolQueryBuilder.must(QueryBuilders.existsQuery(filed));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    boolQueryBuilder.must(nestedQueryBuilder);
                } else {
                    boolQueryBuilder.must(QueryBuilders.existsQuery(filed));
                }
            }
        }
        //或连接，必须不为null的字段
        List<String> shouldExistsFields = req.getShouldExistsFields();
        if (shouldExistsFields != null) {
            for (String filed : shouldExistsFields) {
                if (filed.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = filed.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    nestedBoolQueryBuilder.must(QueryBuilders.existsQuery(filed));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    boolQueryBuilder.should(nestedQueryBuilder);
                } else {
                    boolQueryBuilder.should(QueryBuilders.existsQuery(filed));
                }
            }
        }

        //且连接,必须为null的字段
        List<String> mustNotExistsFields = req.getMustNotExistsFields();
        if (mustNotExistsFields != null) {
            for (String filed : shouldExistsFields) {
                if (filed.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = filed.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    nestedBoolQueryBuilder.must(QueryBuilders.existsQuery(filed));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    boolQueryBuilder.mustNot(nestedQueryBuilder);
                } else {
                    boolQueryBuilder.mustNot(QueryBuilders.existsQuery(filed));
                }
            }
        }

        //范围查询
        Map<String, ReqElasticsearchVo.Range> rangeMap = req.getRangeMap();
        if (rangeMap != null) {
            Set<String> keys = rangeMap.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                ReqElasticsearchVo.Range range = rangeMap.get(key);
                RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(key);
                //设置范围
                rangeQuery.gte(range.getFrom());
                rangeQuery.lte(range.getTo());
                boolQueryBuilder.filter(rangeQuery);
                //.query(rangeQuery);
            }
        }
        /*--------------------------------------------嵌套查询---------------------------------------------------*/
        //不分词,外部且连接,内部或连接,字段单值精确匹配
        BoolQueryBuilder mustShouldTermBuilder = QueryBuilders.boolQuery();
        Map<String, Object> mustShouldTermMap = req.getMustShouldTermMap();
        if (mustShouldTermMap != null) {
            for (Map.Entry<String, Object> entry : mustShouldTermMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = key.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    nestedBoolQueryBuilder.must(QueryBuilders.termQuery(key, value));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    mustShouldTermBuilder.should(nestedQueryBuilder);
                } else {
                    mustShouldTermBuilder.should(QueryBuilders.termQuery(key, value));
                }
            }
            boolQueryBuilder.must(mustShouldTermBuilder);
        }

        //不分词,外部且连接,内部或连接,字段多值精确匹配
        BoolQueryBuilder mustShouldTermsBuilder = QueryBuilders.boolQuery();
        Map<String, List<String>> mustShouldTermsMap = req.getMustShouldTermsMap();
        if (mustShouldTermsMap != null) {
            for (Map.Entry<String, List<String>> entry : mustShouldTermsMap.entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                if (key.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = key.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    nestedBoolQueryBuilder.must(QueryBuilders.termsQuery(key, value));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    mustShouldTermsBuilder.should(nestedQueryBuilder);
                } else {
                    mustShouldTermsBuilder.should(QueryBuilders.termsQuery(key, value));
                }
            }
            boolQueryBuilder.must(mustShouldTermsBuilder);
        }

        //不分词,外部且连接,内部或连接,模糊匹配
        BoolQueryBuilder mustShouldWildcardBuilder = QueryBuilders.boolQuery();
        Map<String, String> mustShouldWildcardMap = req.getMustShouldWildcardMap();
        if (mustShouldWildcardMap != null) {
            for (Map.Entry<String, String> entry : mustShouldWildcardMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.contains(".")) {
                    //如果是嵌套查询找到父字段
                    String parentFiled = key.split("\\.")[0];
                    BoolQueryBuilder nestedBoolQueryBuilder = QueryBuilders.boolQuery();
                    nestedBoolQueryBuilder.must(QueryBuilders.wildcardQuery(key, value));
                    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(parentFiled, nestedBoolQueryBuilder, ScoreMode.None);
                    mustShouldWildcardBuilder.should(nestedQueryBuilder);
                } else {
                    mustShouldWildcardBuilder.should(QueryBuilders.wildcardQuery(key, value));
                }
            }
            boolQueryBuilder.must(mustShouldWildcardBuilder);
        }
        return boolQueryBuilder;
    }

    /**
     * 执行查询
     *
     * @param searchRequest
     * @param builder
     * @param restHighLevelClient
     * @return
     */
    public static SearchResponse executeSearch(SearchRequest searchRequest, SearchSourceBuilder builder, RestHighLevelClient restHighLevelClient) {
        searchRequest.source(builder);
        log.info("es查询DSL========》{}", builder);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("es查询完成，耗时：{}", response.getTook());
        return response;
    }

    /**
     * 获取Elasticsearch实体类上的索引名称
     * @param entity 实体类
     * @return
     */
    public static String getIndexName(Class<? extends Object> entity){
        Document document = entity.getAnnotation(Document.class);
        if(StringUtils.isBlank(document.indexName())){
            throw new CustomException(entity.getSimpleName()+"类上缺少索引注解@Document");
        }
        return document.indexName();
    }

    /**
     * 将查询的Elasticsearch列表转换为页面VO
     * @param resElasticsearchVo
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> ResPageDataVO convertVoToPage(ResElasticsearchVo resElasticsearchVo,Class<T> tClass){
        LinkedHashMap<String, String> data = resElasticsearchVo.getData();
        List<T> list = new ArrayList<>(data.size());
        resElasticsearchVo.getData().forEach((key, value) -> {
            JSONObject entityObject = JSONObject.parseObject(value);
            //将id设置进去
            entityObject.put(docId,key);
            T entity = BeanConvertUtils.dataToEntity(entityObject, tClass);
            list.add(entity);
        });
        return new ResPageDataVO(list,resElasticsearchVo.getTotal());
    }
}
