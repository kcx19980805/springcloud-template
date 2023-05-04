package com.kcx.common.utils.file;

import com.kcx.common.exception.CustomException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件下载工具类
 */
public class FileDownloadUtils {

    /**
     * 下载文件到浏览器
     *
     * @param baseDir  存储文件的基目录
     * @param fileName 文件名称
     * @param response 网页响应对象
     * @param request  网页请求对象
     */
    public static void downloadFileToWeb(String baseDir, String fileName, HttpServletResponse response, HttpServletRequest request) {
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, fileName));
            downloadFile(baseDir + File.separator + fileName, new BufferedOutputStream(response.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件到另一个文件
     * 可以当做是移动文件/复制文件
     *
     * @param resourcePathName 存储的源文件全路径
     * @param targetPathName   将要存储的文件全路径
     * @param deleteResource   是否删除源文件
     */
    public static void downloadFileToFile(String resourcePathName, String targetPathName, boolean deleteResource) {
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(targetPathName));
            downloadFile(resourcePathName, bufferedOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (deleteResource) {
            File file = new File(resourcePathName);
            file.delete();
        }
    }

    /**
     * 下载文件到输出流中
     *
     * @param pathName     文件全路径
     * @param outputStream 输出流
     */
    public static void downloadFile(String pathName, BufferedOutputStream outputStream) {
        File file = new File(pathName);
        if (!file.exists()) {
            throw new CustomException(pathName + "文件不存在");
        }
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(pathName));
            byte[] buffer = new byte[2048];
            int len;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
                if (null != bufferedInputStream) {
                    bufferedInputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 分片下载文件到浏览器,利用多线程使得这个方法比普通下载快，注意下载的单个分片无法使用，需要全部下载完合并
     *
     * @param pathName     存储的源文件全路径
     * @param chunkSize    每个分片的大小，单位byte
     * @param currentChunk 当前分片数-1,0,1,2...如果是-1表示先试探获得文件大小，总分片数
     * @param response     网页响应对象
     * @param request      网页请求对象
     * @return 参数需要从response.getFirstHeader中获取，exists文件是否存在 fileName文件名称 fileSize文件大小 isComplete是否分片下载完成 totalChunk总分片数
     * 文件流需要从response.getEntity().getContent()获取
     */
    public static void splitDownloadFileToWeb(String pathName, int chunkSize, int currentChunk, int totalChunk, HttpServletResponse response, HttpServletRequest request) {
        File file = new File(pathName);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/x-download");
        response.setHeader("Accept-Range", "bytes");
        BufferedOutputStream bufferedOutputStream = null;
        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + FileUtils.setFileDownloadHeader(request, file.getName()));
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            Map<String, Object> map = splitDownloadFile(pathName, chunkSize, currentChunk, totalChunk, bufferedOutputStream);
            response.setHeader("fileName", String.valueOf(map.get("fileName")));
            response.setHeader("totalChunk", String.valueOf(map.get("totalChunk")));
            response.setHeader("fileSize", String.valueOf(map.get("fileSize")));
            response.setHeader("exists", String.valueOf(map.get("exists")));
            response.setHeader("isComplete", String.valueOf(map.get("isComplete")));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                //忽略
            }
        }
    }

    /**
     * 分片下载文件到另一个文件,利用多线程使得这个方法比普通下载快，注意下载的单个分片无法使用，需要全部下载完合并
     * 可以当做是把本地完整文件分片为多个小文件
     *
     * @param pathName       存储的源文件全路径
     * @param chunkSize      每个分片的大小，单位byte
     * @param currentChunk   当前分片数-1,0,1,2...如果是-1表示先试探获得文件大小，总分片数,后续持续请求从0开始
     * @param totalChunk     总分片大小，如果是-1表示先试探获得总分片大小，后续持续请求传入返回的总分片大小
     * @param targetPathName 本次将要存储的分片文件全路径,试探时可以为null
     * @return exists文件是否存在 fileName文件名称 fileSize文件大小 isComplete是否分片下载完成 totalChunk总分片数
     */
    public static Map<String, Object> splitDownloadFileToFile(String pathName, long chunkSize, int currentChunk, int totalChunk, String targetPathName) {
        try {
            if (StringUtils.isBlank(targetPathName)) {
                return splitDownloadFile(pathName, chunkSize, currentChunk, totalChunk, null);
            }
            return splitDownloadFile(pathName, chunkSize, currentChunk, totalChunk, new BufferedOutputStream(new FileOutputStream(targetPathName)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 分片下载文件到输出流中,利用多线程使得这个方法比普通下载快，注意下载的单个分片无法使用，需要全部下载完合并
     *
     * @param pathName     存储的源文件全路径
     * @param chunkSize    每个分片的大小，单位byte
     * @param currentChunk 当前分片数-1,0,1,2...如果是-1表示先试探获得文件大小，总分片数,后续持续请求从0开始
     * @param totalChunk   总分片大小，如果是-1表示先试探获得总分片大小，后续持续请求传入返回的总分片大小
     * @param outputStream 输出流,试探时可以为null
     * @return exists文件是否存在 fileName文件名称 fileSize文件大小 isComplete是否分片下载完成 totalChunk总分片数
     */
    public static Map<String, Object> splitDownloadFile(String pathName, long chunkSize, int currentChunk, int totalChunk, BufferedOutputStream outputStream) {
        File file = new File(pathName);
        boolean exists = file.exists();
        String fileName = file.getName();
        long fileSize = file.length();
        //如果只是先试探获得文件信息,计算分片大小
        if (totalChunk == -1) {
            totalChunk = (int) (fileSize / chunkSize) + (fileSize % chunkSize > 0 ? 1 : 0);
        } else if (currentChunk > totalChunk - 1) {
            throw new CustomException("请求分片大小超出限制");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("exists", exists);
        result.put("fileName", fileName);
        result.put("fileSize", fileSize);
        result.put("isComplete", false);
        result.put("totalChunk", totalChunk);
        //如果只是先试探获得文件信息
        if (currentChunk == -1) {
            return result;
        }
        InputStream is = null;
        OutputStream os = null;
        try {
            long endPos = (currentChunk + 1) * chunkSize;
            long startPos = endPos - chunkSize;
            //这次请求已经超过了文件末尾，已经下载完成
            if (startPos >= fileSize) {
                result.put("isComplete", true);
                return result;
            }
            if (endPos > fileSize) {
                result.put("isComplete", true);
                endPos = fileSize;
            }
            long downloadByte = 0;
            //当前要截取的文件字节范围
            long rangeLength = endPos - startPos;
            os = outputStream;
            is = new BufferedInputStream(new FileInputStream(file));
            //跳过起始位置之前的字节
            is.skip(startPos);
            byte[] buffer = new byte[2048];
            int length = 0;
            while (downloadByte < rangeLength) {
                //这里每次读取2kb，如果是最后不足2kb，读取剩余的
                length = is.read(buffer, 0, (rangeLength - downloadByte) <= buffer.length ? (int) (rangeLength - downloadByte) : buffer.length);
                downloadByte += length;
                os.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 要测试先注释掉包含HttpServletResponse导出的方法
     */
    public static void main(String[] args) {
/*        long start = System.currentTimeMillis();
        downloadToFile("D:\\aaa.mp4","D:\\test.mp4",false);
        System.out.println("用时:" + (System.currentTimeMillis() - start) / 1000);*/

        //分片下载
        Map<String, Object> map = splitDownloadFileToFile("D:\\xxx.mp4",
                50 * 1024 * 1024, -1, -1, "");
        String fileName = (String) map.get("fileName");
        int totalChunk = (int) map.get("totalChunk");
        long fileSize = (long) map.get("fileSize");
        boolean exists = (boolean) map.get("exists");
        boolean isComplete = (boolean) map.get("isComplete");
        //分片下载文件,可多线程替换循环
        for (int i = 0; i < totalChunk; i++) {
            Map<String, Object> objectMap = splitDownloadFileToFile("D:\\xxx.mp4",
                    50 * 1024 * 1024, i, totalChunk, "D:\\" + i + "_" + fileName);
            if (i == totalChunk - 1) {
                System.out.println("文件下载完成：" + objectMap.get("isComplete"));
            }
        }

        //先获取文件信息
/*        Map<String,Object> map = splitDownloadFile("xxx.mp4",
                50 * 1024 * 1024, -1, -1, null);
        String fileName = (String) map.get("fileName");
        int totalChunk = (int) map.get("totalChunk");
        long fileSize = (long) map.get("fileSize");
        boolean exists = (boolean) map.get("exists");
        boolean isComplete = (boolean) map.get("isComplete");
        //分片下载文件,可多线程替换循环
        for (int i = 0; i < totalChunk; i++) {
            try {
                Map<String,Object> objectMap =splitDownloadFile("D:\\xxx.mp4",
                        50 * 1024 * 1024,i,totalChunk,new BufferedOutputStream(new FileOutputStream("D:\\"+i+"_"+fileName)));
                if(i==totalChunk-1){
                    System.out.println("文件下载完成：" +objectMap.get("isComplete"));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }*/


    }
}
