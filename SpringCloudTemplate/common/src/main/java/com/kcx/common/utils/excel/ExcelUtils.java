package com.kcx.common.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kcx.common.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Excel操作工具类
 */
public class ExcelUtils {

    /**
     * 导出Excel到浏览器
     * @param fileName 文件名称
     * @param entityListMap 多个Sheet,key为Sheet名称，value为Sheet数据
     * @param response 网页响应对象
     * @param ? 属性带有@ExcelProperty注解的实体类
     */
    public static void exportExcelToWeb(String fileName, Map<String,List<? extends Object>> entityListMap, HttpServletResponse response){
        ExcelWriter excelWriter = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            //这里URLEncoder.encode可以防止中文乱码
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            excelWriter = EasyExcel.write(response.getOutputStream()).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeExcel(entityListMap,excelWriter);
    }

    /**
     * 导出excel到文件
     * @param pathName 文件的全路径 如d:\test.xlsx
     * @param entityListMap 多个Sheet,key为Sheet名称，value为Sheet数据
     * @param ? 属性带有@ExcelProperty注解的实体类
     */
    public static void exportExcelToFile(String pathName,Map<String,List<? extends Object>> entityListMap){
        ExcelWriter excelWriter = EasyExcel.write(pathName).build();
        writeExcel(entityListMap,excelWriter);
    }

    /**
     * 写入数据到excel
     * @param entityListMap
     * @param excelWriter
     * @param ?
     */
    public static void writeExcel(Map<String,List<? extends Object>> entityListMap,ExcelWriter excelWriter){
        try {
            AtomicInteger num = new AtomicInteger(0);
            entityListMap.entrySet().stream().forEach(entry->{
                List dataList = entry.getValue();
                if(dataList != null && dataList.size()>0){
                    String sheetName = entry.getKey();
                    Class<?> entityClass = dataList.get(0).getClass();
                    WriteSheet writeSheet = EasyExcel.writerSheet(num.get(),sheetName)
                            //设置列宽
                            .registerWriteHandler(new ExcelAutoWidth())
                            //.includeColumnFiledNames(getExportFields(entityClass))
                            .build();
                    WriteTable writeTable = EasyExcel.writerTable(num.getAndIncrement())
                            //设置表头
                            .head(entityClass)
                            .needHead(true)
                            .build();
                    writeSheet.setSheetName(sheetName);
                    excelWriter.write(dataList,writeSheet,writeTable);
                }
            });
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Excel文件导出失败");
        }
    }

    /**
     * 导入Excel从浏览器
     * 注意entityListMap的key是不能重复的，也就是多个sheet对应的实体类不能相同
     * @param multipartFile 浏览器传入的参数File
     * @param entityListMap 多个Sheet,key为实体类，value为具体处理解析文件类，注意excel文件的每个sheet与map存储的顺序要一一对应
     * @param <?> 属性带有@ExcelProperty注解的实体类
     */
    public static void importExcelFromWeb(MultipartFile multipartFile,Map<Class<?>,AnalysisEventListener> entityListMap){
        try {
            AtomicInteger num = new AtomicInteger(0);
            entityListMap.entrySet().stream().forEach(entry->{
                try {
                    EasyExcel.read(multipartFile.getInputStream(),entry.getKey(),entry.getValue()).sheet(num.getAndIncrement()).doRead();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new CustomException("Excel文件导入失败");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入Excel从本地文件
     * 注意entityListMap的key是不能重复的，也就是多个sheet对应的实体类不能相同
     * @param pathName 文件的全路径 如d:\test.xlsx
     * @param entityListMap 多个Sheet,key为实体类，value为具体处理解析文件类，注意excel文件的每个sheet与map存储的顺序要一一对应
     * @param <?> 属性带有@ExcelProperty注解的实体类
     */
    public static void importExcelFromFile(String pathName,Map<Class<?>,AnalysisEventListener> entityListMap){
        try {
            AtomicInteger num = new AtomicInteger(0);
            entityListMap.entrySet().stream().forEach(entry->{
                try {
                    EasyExcel.read(new FileInputStream(pathName),entry.getKey(),entry.getValue()).sheet(num.getAndIncrement()).doRead();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new CustomException("Excel文件导入失败");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入Excel从网络文件
     * 注意entityListMap的key是不能重复的，也就是多个sheet对应的实体类不能相同
     * @param url 网络上的一个文件地址
     * @param entityListMap 多个Sheet,key为实体类，value为具体处理解析文件类，注意excel文件的每个sheet与map存储的顺序要一一对应
     * @param <?> 属性带有@ExcelProperty注解的实体类
     */
    public static <T> void importExcelFromURL(String url,Map<Class<?>,AnalysisEventListener> entityListMap){
        try {
            AtomicInteger num = new AtomicInteger(0);
            URL urls = new URL(url);
            entityListMap.entrySet().stream().forEach(entry->{
                try { ;
                    EasyExcel.read(urls.openStream(),entry.getKey(),entry.getValue()).sheet(num.getAndIncrement()).doRead();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new CustomException("Excel文件导入失败");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从类中得到需要导出的字段,解析有JsonProperty注解的字段
     * @param clazz
     * @return
     */
    public static Set<String> getExportFields(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        Set<String> column = new HashSet<>(declaredFields.length);
        for (Field field : declaredFields) {
            String name = field.getName();
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            ExcelIgnore excelIgnore = field.getAnnotation(ExcelIgnore.class);
            if (null != excelProperty && null == excelIgnore) {
                column.add(name);
            }
        }
        return column;
    }


    /*-------------------------------------测试-------------------------------------------*/
    @Data
    @AllArgsConstructor
    @NoArgsConstructor//AllArgsConstructor会删除自带的无参构造器，一定要加这个，否则无法实例化
    public static class TestEntity{
        @ExcelProperty("姓名")
        private String name;
        @ExcelProperty("年龄")
        private int sex;
        @ExcelProperty("简介")
        private String desc;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TestEntity2{
        @ExcelProperty("城市")
        private String city;
        @ExcelProperty("编码")
        private int code;
        @ExcelProperty("简介")
        private String desc;
    }

    public static class TestEntityImportListener extends AnalysisEventListener<TestEntity>{
        List<TestEntity> list = new ArrayList<>();
        @Override
        public void invoke(TestEntity data, AnalysisContext context) {
            list.add(data);
        }
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            System.out.println("所有数据解析如下：");
            for(TestEntity item : list){
                System.out.println(item.toString());
            }
        }
    }

    public static class TestEntityImportListener2 extends AnalysisEventListener<TestEntity2>{
        List<TestEntity2> list = new ArrayList<>();
        @Override
        public void invoke(TestEntity2 data, AnalysisContext context) {
            list.add(data);
        }
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            System.out.println("所有数据解析如下：");
            for(TestEntity2 item : list){
                System.out.println(item.toString());
            }
        }
    }

    /**
     * 要测试先注释掉包含HttpServletResponse导出的方法
     */
    public static void main(String[] args) {
        HashMap<String,List<? extends Object>> map = new HashMap<>();
        List<TestEntity> list1 = new ArrayList<>();
        list1.add(new TestEntity("小明",18,"小明喜欢打游戏"));
        list1.add(new TestEntity("小明2",18,"小明喜欢打游戏2"));
        List<TestEntity2> list2 = new ArrayList<>();
        list2.add(new TestEntity2("成都",1001,"成都一座休闲城市"));
        list2.add(new TestEntity2("上海",1002,"上海是魔都"));
        map.put("sheet1",list1);
        map.put("sheet2",list2);
        exportExcelToFile("D:\\test.xlsx",map);

        Map<Class<?>,AnalysisEventListener> entityListMap = new LinkedHashMap<>();
        entityListMap.put(TestEntity.class,new TestEntityImportListener());
        entityListMap.put(TestEntity2.class,new TestEntityImportListener2());
        importExcelFromFile("D:\\test.xlsx",entityListMap);
    }
}
