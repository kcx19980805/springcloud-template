package com.kcx.common.utils.file;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文件工具类
 */
public class ZipFileUtils {

    /**
     * 下载压缩文件到浏览器
     * @param pathNameList 要压缩的文件所在路径，可以是文件夹或具体文件
     * @param zipName 压缩文件名称
     * @param response 网页响应对象
     * @param request 网页请求对象
     */
    public static void downloadZipFileToWeb(List<String> pathNameList, String zipName, HttpServletResponse response, HttpServletRequest request) {
        //响应头的设置
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        String downloadName = zipName + ".zip";
        //返回客户端浏览器的版本号、类型
        String agent = request.getHeader("USER-AGENT");
        try {
            //针对IE或者以IE为内核的浏览器：
            if (agent.contains("MSIE") || agent.contains("Trident")) {
                downloadName = java.net.URLEncoder.encode(downloadName, "UTF-8");
            } else {
                //非IE浏览器的处理：
                downloadName = new String(downloadName.getBytes("UTF-8"), "ISO-8859-1");
            }
            response.setHeader("Content-Disposition", "attachment;fileName=\"" + downloadName + "\"");
            //设置压缩流
            ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
            //设置压缩方法
            zipOutputStream.setMethod(ZipOutputStream.DEFLATED);
            zipFiles(pathNameList, zipOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载压缩文件到本地文件
     * @param pathNameList 要压缩的文件所在路径，可以是文件夹或具体文件
     * @param zipPathName 压缩文件的全路径 如d:\test.zip
     */
    public static void downloadZipFileToFile(List<String> pathNameList, String zipPathName){
        try {
            File target = new File(zipPathName);
            target.setWritable(true, true);
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
            }
            //设置压缩流
            ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipPathName)));
            //设置压缩方法
            zipOutputStream.setMethod(ZipOutputStream.DEFLATED);
            zipFiles(pathNameList,zipOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩文件
     * @param pathNameList 要压缩的文件所在路径，可以是文件夹或具体文件
     * @param zipOutputStream 压缩输出流
     */
    public static void zipFiles(List<String> pathNameList, ZipOutputStream zipOutputStream) {
        byte[] buffer = new byte[2048];
        for(String pathName : pathNameList){
            File file = new File(pathName);
            if (!file.exists()){
                continue;
            }
            if(file.isFile()){
                BufferedInputStream bis = null;
                try {
                    String fileName = file.getName();
                    //创建输入流读取文件
                    bis = new BufferedInputStream(new FileInputStream(file));
                    //将文件写入zip内，即将文件进行打包
                    zipOutputStream.putNextEntry(new ZipEntry(fileName));
                    int size;
                    //设置读取数据缓存大小
                    while ((size = bis.read(buffer)) != -1) {
                        zipOutputStream.write(buffer, 0, size);
                    }
                    //关闭输入输出流
                    zipOutputStream.closeEntry();
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(file.isDirectory()){
                //如果是文件夹，则使用穷举的方法获取文件，写入zip
                File[] files = file.listFiles();
                List<String> pathNameListItem = new ArrayList<>();
                for (File fileItem : files) {
                    pathNameListItem.add(fileItem.toString());
                }
                zipFiles(pathNameListItem, zipOutputStream);
            }
        }
        if (null != zipOutputStream) {
            try {
                zipOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 要测试先注释掉包含HttpServletResponse导出的方法
     */
    public static void main(String[] args) {
        List<String> pathNameList = new ArrayList<>();
       // pathNameList.add("D:\\账号密码.txt");
        pathNameList.add("D:\\新建文本文档.txt");
      //  pathNameList.add("D:\\音乐2");
        downloadZipFileToFile(pathNameList,"D:/test.zip");
    }
}
