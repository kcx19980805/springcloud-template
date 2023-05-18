package com.kcx.common.utils.elasticsearch.requestVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;
import java.util.Map;

/**
 * Elasticsearch通用查询实体
 * 嵌套查询时要设置字段的mapping属性"type" : "nested"
 * 分词查询时要设置字段的mapping属性type : text,analyzer : 分词器类型,如ik_max_word
 * 模糊查询时需要设置mapping属性"type" : "wildcard"
 */
@Data
public class ReqElasticsearchVo {
  /**
   * 当前页
   */
  private int pageNo = 1;
  /**
   * 每页大小
   */
  private int pageSize = 10;
  /**
   * 且连接 must term不分词匹配 字段与值一对一 支持嵌套字段查询（user.name）
   * key 字段
   * value 值
   */
  private Map<String, Object> mustTermMap;
  /**
   * 或连接 should term不分词匹配  字段与值一对一 支持嵌套字段查询（user.name）
   * key 字段
   * value 值
   */
  private Map<String, Object> shouldTermMap;
  /**
   * 且连接 must terms不分词匹配 字段与值一对多 支持嵌套字段查询（user.name）
   * key 字段
   * value 值
   */
  private Map<String, List<Object>> mustTermsMap;
  /**
   * 或连接 should terms不分词匹配 字段与值一对多 支持嵌套字段查询（user.name）
   * key 字段
   * value 值
   */
  private Map<String, List<Object>> shouldTermsMap;
  /**
   * 且连接 must wildcard模糊匹配 支持嵌套字段查询（user.name）
   * key 字段
   * value 值
   */
  private Map<String, String> mustWildcardMap;
  /**
   * 或连接 should wildcard模糊匹配 支持嵌套字段查询（user.name）
   * key 字段
   * value 值
   */
  private Map<String, String> shouldWildcardMap;
  /**
   * 且连接 must match分词匹配 字段与值一对一 支持嵌套字段查询（user.name）
   * key 字段
   * value 值
   */
  private Map<String, Object> mustMatchMap;
  /**
   * 或连接 should match分词匹配 字段与值一对一 支持嵌套字段查询（user.name）
   * key 字段
   * value 值
   */
  private Map<String, Object> shouldMatchMap;
  /**
   * 且连接 must match_phrase分词匹配 字段与值一对一 支持嵌套字段查询（user.name）
   * 要求所有的分词必须同时出现在文档中，同时位置必须紧邻一致
   * key 字段
   * value 值
   */
  private Map<String, Object> mustMatchPhraseMap;
  /**
   * 或连接 should match_phrase分词匹配 字段与值一对一 支持嵌套字段查询（user.name）
   * 要求所有的分词必须同时出现在文档中，同时位置必须紧邻一致
   * key 字段
   * value 值
   */
  private Map<String, Object> shouldMatchPhraseMap;
  /**
   * 且连接 must multi_match字段匹配 字段与值多对一匹配
   * key 值
   * value 字段
   */
  private Map<Object, List<String>> mustMultiMatchMap;
  /**
   * 或连接 must multi_match字段匹配 字段与值多对一匹配
   * key 值
   * value 字段
   */
  private Map<Object, List<String>> shouldMultiMatchMap;
  /**
   * 且连接 must_not term不分词,排除对应值 字段与值一对一 支持嵌套字段查询（user.name）
   * key 字段
   * value 值
   */
  private Map<String, Object> mustNotTermMap;
  /**
   * 且连接 must exists必须存在,不为null的字段
   */
  private List<String> mustExistsFields;
  /**
   * 或连接 must exists必须存在,不为null的字段
   */
  private List<String> shouldExistsFields;
  /**
   * 且连接 must_not exists必须不存在,为null的字段
   */
  private List<String> mustNotExistsFields;
  /**
   *  collapse要去重的字段
   */
  private String collapseField;
  /**
   * sort排序 字段与值一对一匹配
   * key 字段
   * value 排序方式 asc/desc
   */
  private Map<String, SortOrder> sortMap;
  /**
   * 要高亮字段
   */
  private String highlightFiled;
  /**
   * range范围查询 过滤 字段与值一对一匹配
   * key 字段
   * value 范围
   */
  private Map<String, Range> rangeMap;
  /**
   * 返回结果包含的字段
   */
  private List<String> includeFields;
  /**
   * 返回结果排除的字段
   */
  private List<String> excludeFields;
  /*============================嵌套bool查询=======================================*/
  /**
   * 且连接 嵌套bool查询 内部或连接 should term不分词匹配  字段与值一对一 支持嵌套字段查询（user.name）
   * 相当于... and (... or ...)
   * key 字段
   * value 值
   */
  private Map<String, Object> mustShouldTermMap;

  /**
   * 且连接 嵌套bool查询 内部或连接 should terms不分词匹配  字段与值一对多  支持嵌套字段查询（user.name）
   * 相当于... and (... or ...)
   * key 字段
   * value 值
   */
  private Map<String, List<String>> mustShouldTermsMap;

  /**
   * 且连接 嵌套bool查询 内部或连接 should wildcard模糊匹配 字段与值一对一 支持嵌套字段查询（user.name）
   * 相当于... and (... or ...)
   * key 字段
   * value 值
   */
  private Map<String, String> mustShouldWildcardMap;

  /**
   * 查询范围
   */
  @Data
  @AllArgsConstructor
  public static class Range {
    /**
     * 结果将大于等于这个值
     */
    private Object from;
    /**
     * 结果将小于等于这个值
     */
    private Object to;
  }
}
