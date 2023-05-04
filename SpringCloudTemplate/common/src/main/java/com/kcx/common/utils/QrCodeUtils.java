package com.kcx.common.utils;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 生成二维码工具
 * 将二维码url转为图片
 */
public class QrCodeUtils {
    /**
     * 生成二维码图片输出到浏览器
     * @param codeUrl 二维码url
     * @param width 图片宽 px
     * @param height 图片高 px
     * @param response 网页响应对象
     * @return
     */
    public static void generateQrcodeToWeb( String codeUrl, int width, int height, HttpServletResponse response){
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            generateQrcode(codeUrl,width,height,bufferedOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != bufferedOutputStream){
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    //忽略
                }
            }
        }
    }

    /**
     * 生成二维码图片保存在本地文件
     * @param codeUrl 二维码url
     * @param width 图片宽 px
     * @param height 图片高 px
     * @param pathName 文件全路径
     * @return
     */
    public static void generateQrcodeToFile(String codeUrl, int width,int height,String pathName){
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(pathName));
            generateQrcode(codeUrl,width,height,bufferedOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != bufferedOutputStream){
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    //忽略
                }
            }
        }
    }

    /**
     * 生成二维码
     * @param codeUrl 二维码url
     * @param width 图片宽 px
     * @param height 图片高 px
     * @param bufferedOutputStream 输出流
     */
    public static void generateQrcode(String codeUrl, int width,int height, BufferedOutputStream bufferedOutputStream){
        //1.初始化参数
        Map<EncodeHintType,Object> map=new LinkedHashMap<>();
        //编码格式
        map.put(EncodeHintType.CHARACTER_SET,"UTF-8");
        //容错率
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置外边距
        map.put(EncodeHintType.MARGIN,1);
        //2.构建位图
        try {
            //1.二维码的内容2.格式3.宽4.高5.参数
            BitMatrix bitMatrix=new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE,width,height,map);
            //设置颜色
            MatrixToImageConfig config=new MatrixToImageConfig(0xFF000000,0xFFFFFFFF);
            MatrixToImageWriter.writeToStream(bitMatrix,"png",bufferedOutputStream,config);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
            String outPutPath = "D:/QrCode.JPG";
            generateQrcodeToFile("https://qr.alipay.com/bax088841lq9tlmt9fkg0078" ,200,200, outPutPath);
    }

}
