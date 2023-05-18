package com.kcx.common.utils.file;



import com.kcx.common.exception.CustomException;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 图片压缩工具类
 */
public class ImageCompressUtils {
    /**
     * 下载压缩图片到浏览器
     * @param pathName 存储的源文件全路径
     * @param scale 压缩比例，0.1压缩宽高为原来的1/10,0.5压缩为原来的1/2,可以为null自动压缩
     * @param response 网页响应对象
     * @param request  网页请求对象
     */
    public static void compressToWeb(String pathName,Double scale, HttpServletResponse response, HttpServletRequest request){
        File file = new File(pathName);
        try {
            response.setCharacterEncoding("utf-8");
            //自动判断下载文件类型
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, file.getName()));
            downloadCompressFile(pathName,scale, new BufferedOutputStream(response.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载压缩图片到文件
     * @param resourcePathName 存储的源文件全路径
     * @param targetPathName   将要存储模糊图片的文件全路径
     * @param scale 压缩比例，0.1压缩宽高为原来的1/10,0.5压缩为原来的1/2,可以为null自动压缩
     */
    public static void compressToFile(String resourcePathName, String targetPathName,Double scale){
        try {
            downloadCompressFile(resourcePathName,scale, new BufferedOutputStream(new FileOutputStream(targetPathName)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



    /**
     * 下载压缩图片到输出流中
     * @param pathName 存储的源文件全路径
     * @param scale 压缩比例，0.1压缩宽高为原来的1/10,0.5压缩为原来的1/2,可以为null自动压缩
     * @param outputStream 输出流
     */
    public static void downloadCompressFile(String pathName,Double scale, BufferedOutputStream outputStream) {
        File file = new File(pathName);
        if (!file.exists()) {
            throw new CustomException(pathName + "文件不存在");
        }
        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(pathName));
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int len;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer,0, len);
            }
            byte[] image;
            if(null != scale){
                image = imageByteCompress(byteArrayOutputStream.toByteArray(),FileUtils.getFileType(pathName),scale);
            }else{
                image = imageBytesAutoCompress(byteArrayOutputStream.toByteArray(),FileUtils.getFileType(pathName));
            }
            outputStream.write(image);
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
                if (null != byteArrayOutputStream) {
                    byteArrayOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩图片
     * @param imageBytes 源图片的字节
     * @param suffix 图片后缀，可以为null,默认jpeg
     * @param scale 压缩比例，0.1压缩宽高为原来的1/10,0.5压缩为原来的1/2,可以为null,默认压缩1/2
     * @return 压缩后的图片字节
     */
    public static byte[] imageByteCompress(byte[] imageBytes, String suffix,Double scale) {
        if(null == scale){
            scale = 0.5;
        }
        return imageCompress(imageBytes, suffix, scale);
    }

    /**
     * 根据图片大小自动压缩,图片大小始终保持200kb左右
     *
     * @param imageBytes 源图片的字节
     * @param suffix 图片后缀，可以为null,默认jpeg
     * @return 压缩后的图片字节
     */
    public static byte[] imageBytesAutoCompress(byte[] imageBytes, String suffix) {
        if (imageBytes.length > 2097152) {
            // 如果图片大于 2MB，压缩为原来的十分之一
            return imageCompress(imageBytes, suffix, 0.1);
        } else if (imageBytes.length > 1048576) {
            // 如果图片大于 1MB，压缩为原来的五分之一
            return imageCompress(imageBytes, suffix, 0.2);
        } else if (imageBytes.length > 512000) {
            // 如果图片大于 500KB，压缩为原来的二分之一
            return imageCompress(imageBytes, suffix, 0.5);
        } else {
            return imageBytes;
        }
    }

    /**
     * 图片压缩
     *
     * @param imageBytes 要压缩图片的字节
     * @param backname   图片格式，如jpeg
     * @param scale      压缩大小，如十分之一，传0.1
     * @return 压缩后的图片字节
     */
    public static byte[] imageCompress(byte[] imageBytes, String backname, Double scale) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
        Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(byteArrayInputStream).scale(scale);
        try {
            BufferedImage bufferedImage = builder.asBufferedImage();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, backname, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageBytes;
    }

    public static void main(String[] args) {
       // compressToFile("D:\\111.jpg","D:\\112.jpg",null);
    }
}
