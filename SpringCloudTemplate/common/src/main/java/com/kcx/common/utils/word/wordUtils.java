package com.kcx.common.utils.word;

import lombok.Data;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * word文件工具类
 */
public class wordUtils {

    /**
     * 输出word到浏览器
     * @param response 浏览器响应对象
     * @param paragraphList 段落列表
     * @param fileName 文件名称
     */
    public static void writeWordToFile(HttpServletResponse response,List<WordParagraphEntity> paragraphList,String fileName){
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ";" + "filename*=utf-8''" + fileName);
            response.setContentType("application/octet-stream");
            writeWord(paragraphList,new BufferedOutputStream(response.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写word到本地文件
     * @param paragraphList 段落列表
     * @param pathName 文件全路径
     */
    public static void writeWordToFile(List<WordParagraphEntity> paragraphList,String pathName){
        try {
            writeWord(paragraphList,new BufferedOutputStream(new FileOutputStream(pathName)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写word文件
     * @param paragraphList 段落列表
     * @param bufferedOutputStream 输出流
     */
    public static void writeWord(List<WordParagraphEntity> paragraphList,BufferedOutputStream bufferedOutputStream){
        //创建Word文件
        XWPFDocument doc = new XWPFDocument();
        paragraphList.stream().forEach(wordParagraphEntity -> {
            //新建段落
            XWPFParagraph xwpfParagraph = doc.createParagraph();
            //设置段落的对齐方式
            xwpfParagraph.setAlignment(wordParagraphEntity.paragraphAlignment);
            //段落内容
            XWPFRun xwpfRun = xwpfParagraph.createRun();
            //设置字体粗体
            xwpfRun.setBold(wordParagraphEntity.bold);
            //设置字体颜色
            xwpfRun.setColor(wordParagraphEntity.fontColor);
            //设置字体大小
            xwpfRun.setFontSize(wordParagraphEntity.fontSize);
            List<String> textList = wordParagraphEntity.getTextList();
            for (String text : textList){
                if(!"\n".equals(text)){
                    xwpfRun.setText(text);
                }
                //回车换行
                xwpfRun.addCarriageReturn();
            }
        });
        try {
            doc.write(bufferedOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bufferedOutputStream != null){
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    //忽略
                }
            }
        }
    }

    /**
     * word文档每个段落的简单实体类
     */
    @Data
    public static class WordParagraphEntity{
        /**
         * 段落的对齐方式,枚举值
         */
        private ParagraphAlignment paragraphAlignment;
        /**
         * 该段落每一行的文本内容，写完后自动换行，如果这行只有\n表示空行
         */
        private List<String> textList;
        /**
         * 是否为粗体
         */
        private boolean bold;
        /**
         * 字体颜色RGB，可以为null
         */
        private String fontColor;
        /**
         * 字体大小，px
         */
        private int fontSize;
    }
    /**
     * 要测试先注释掉包含HttpServletResponse导出的方法
     */
    public static void main(String[] args) {
/*        List<WordParagraphEntity> paragraphList = new ArrayList<>();
        WordParagraphEntity wordParagraphEntity1 = new WordParagraphEntity();
        wordParagraphEntity1.setBold(true);
        wordParagraphEntity1.setParagraphAlignment(ParagraphAlignment.CENTER);
        wordParagraphEntity1.setFontSize(20);
        wordParagraphEntity1.setFontColor(null);
        List<String> textList1 = new ArrayList<>();
        textList1.add("业务办理授权函");
        textList1.add("\n");
        wordParagraphEntity1.setTextList(textList1);
        paragraphList.add(wordParagraphEntity1);

        WordParagraphEntity wordParagraphEntity2 = new WordParagraphEntity();
        wordParagraphEntity2.setBold(false);
        wordParagraphEntity2.setParagraphAlignment(ParagraphAlignment.BOTH);
        wordParagraphEntity2.setFontSize(12);
        wordParagraphEntity2.setFontColor(null);
        List<String> textList2 = new ArrayList<>();
        textList2.add("财付通支付科技有限公司：");
        textList2.add("\t\t我单位及法定代表人/负责人向贵司确认授权我单位员工______（身份证号码_____________________，任职部门___________，职务_________），代表我单位向贵公司申请开通微信支付商户号及进行后续商户号相关操作。我单位及法定代表人/负责人确保向贵司提供资料的真实性、准确性、合法性和完整性，请贵司协助办理相关手续。该员工操作微信支付商户号的所有行为均视为我单位行为，由我单位承担所有责任。");
        textList2.add("法定代表人姓名：");
        textList2.add("身份证号码：");
        textList2.add("身份证有效期：需填写开始日期和结束日期");
        textList2.add("\n");
        textList2.add("特此确认。");
        textList2.add("\n");
        textList2.add("\n");
        textList2.add("                                    单位盖章：");
        textList2.add("                                    单位名称：");
        textList2.add("                                    法定代表人/负责人章或签字：");
        textList2.add("                                    年    月    日");
        wordParagraphEntity2.setTextList(textList2);
        paragraphList.add(wordParagraphEntity2);
        writeWordToFile(paragraphList,"D:\\业务办理授权函模板3.doc");*/
    }
}
