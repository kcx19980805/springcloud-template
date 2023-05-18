package com.kcx.common.utils.file;

import com.kcx.common.exception.CustomException;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.*;

/**
 * 图片模糊处理工具类
 */
public class ImageFuzzyUtils {

    /**
     * 下载模糊处理的图片到浏览器
     * @param pathName 存储的源文件全路径
     * @param radius 模糊权重，越大越模糊,可以为null,默认5
     * @param response 网页响应对象
     * @param request  网页请求对象
     */
    public static void fuzzyImageToWeb(String pathName,Integer radius, HttpServletResponse response, HttpServletRequest request){
        File file = new File(pathName);
        try {
            response.setCharacterEncoding("utf-8");
            //自动判断下载文件类型
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, file.getName()));
            downloadFuzzyFile(pathName,radius, new BufferedOutputStream(response.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载模糊处理的图片到文件
     * @param resourcePathName 存储的源文件全路径
     * @param targetPathName   将要存储模糊图片的文件全路径
     * @param radius 模糊权重，越大越模糊,可以为null,默认5
     */
    public static void fuzzyImageToFile(String resourcePathName, String targetPathName, Integer radius){
        try {
            downloadFuzzyFile(resourcePathName,radius, new BufferedOutputStream(new FileOutputStream(targetPathName)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载模糊图片到输出流中
     * @param pathName 将要存储模糊图片的文件全路径
     * @param radius 模糊权重，越大越模糊,可以为null,默认5
     * @param outputStream 输出流
     */
    public static void downloadFuzzyFile(String pathName,Integer radius, BufferedOutputStream outputStream) {
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
            byte[] image = fuzzyImage(byteArrayOutputStream.toByteArray(),FileUtils.getFileType(pathName),radius);
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
     * 将图片输出为模糊图片
     * @param imageBytes 图片字节
     * @param suffix  图片后缀，可以为null,默认jpeg
     * @param radius 模糊权重,可以为null,默认5
     * @return 模糊后的图片字节
     */
    public static byte[] fuzzyImage(byte[] imageBytes, String suffix, Integer radius) {
        try {
            if(StringUtils.isBlank(suffix)){
                suffix = "jpeg";
            }
            if(null == radius){
                radius = 5;
            }
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            BufferedImage img = ImageIO.read(byteArrayInputStream);
            int height = img.getHeight();
            int width = img.getWidth();
            int[] pixelMatrix = getImagePixelMatrix(img, width, height);
            pixelMatrix = gaussianFuzzy(pixelMatrix, width, height, radius);
            img.setRGB(0, 0, width, height, pixelMatrix, 0, width);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(img, suffix, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图像像素矩阵
     * @param im 图像
     * @param w 图像宽px
     * @param h 图像高px
     * @return 图像像素矩阵
     */
    private static int[] getImagePixelMatrix(Image im, int w, int h) {
        int[] pix = new int[w * h];
        PixelGrabber pg = null;
        try {
            pg = new PixelGrabber(im, 0, 0, w, h, pix, 0, w);
            if (!pg.grabPixels()) {
                try {
                    throw new AWTException("pg error" + pg.status());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pix;
    }

    /**
     * 高斯模糊算法
     * @param pix 源图像矩阵
     * @param w 图像宽
     * @param h 图像高
     * @param radius 模糊权重
     * @return 模糊处理后的图像矩阵
     */
    public static int[] gaussianFuzzy(int[] pix, int w, int h, int radius) {
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;
        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];
        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }
        yw = yi = 0;
        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;
        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;
            for (x = 0; x < w; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;
                sir = stack[i + radius];
                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];
                rbs = r1 - Math.abs(i);
                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];
                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi += w;
            }
        }
        return pix;
    }

    /**
     * 要测试先注释掉包含HttpServletResponse导出的方法
     */
    public static void main(String[] args) {
        //fuzzyImageToFile("D:\\1.jpg","D:\\2.jpg",10);
    }
}
