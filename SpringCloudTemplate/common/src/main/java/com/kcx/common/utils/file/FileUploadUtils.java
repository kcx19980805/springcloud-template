package com.kcx.common.utils.file;

import com.kcx.common.constant.Constants;
import com.kcx.common.exception.CustomException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文件上传工具类
 */
public class FileUploadUtils {

    /**
     * 浏览器文件上传
     *
     * @param baseDir     存储文件的基目录
     * @param file        上传的文件,可直接通过接口接收或((MultipartHttpServletRequest) request).getFiles
     * @param maxFileSize 文件最大大小,单位byte
     * @return 存储文件全路径
     */
    public static final String uploadFileFromWeb(String baseDir, MultipartFile file, long maxFileSize) throws IOException {
        return uploadFileFromWeb(baseDir, file, maxFileSize, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
    }

    /**
     * 浏览器文件上传
     *
     * @param baseDir          存储文件的基目录
     * @param file             上传的文件,可直接通过接口接收或((MultipartHttpServletRequest) request).getFiles
     * @param maxFileSize      文件最大大小,单位byte
     * @param allowedExtension 上传文件允许的后缀类型 MimeTypeUtils常量
     * @return 存储文件全路径
     */
    public static final String uploadFileFromWeb(String baseDir, MultipartFile file, long maxFileSize, String[] allowedExtension) throws IOException {
        //检查文件大小是否超出限制
        long size = file.getSize();
        if (size > maxFileSize) {
            throw new CustomException("文件大小超出限制，最大为" + maxFileSize / 1024 / 1024 + "M");
        }
        //文件名后缀
        String extension = com.kcx.common.utils.file.FileUtils.getExtension(file);
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        //检查文件后缀是否是符合要求
        checkExtension(extension, allowedExtension);
        //编码文件名
        String fileName = DateFormatUtils.format(new Date(), "yyyy-MM-dd-HHmmss-") + RandomStringUtils.randomAlphabetic(4)
                + "-" + file.getName() + extension;
        File desc = new File(baseDir + File.separator + fileName);
        desc.setWritable(true, true);
        //创建目录
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        //创建文件
        if (!desc.exists()) {
            desc.createNewFile();
        }
        //transferTo方法将文件复制到本地
        file.transferTo(desc);
        return desc.toString();
    }

    /**
     * 从网络url中上传文件
     *
     * @param baseDir          存储文件的基目录
     * @param extension        文件后缀
     * @param url              url地址
     * @param maxFileSize      文件最大大小,单位byte
     * @param allowedExtension 上传文件允许的后缀类型 MimeTypeUtils常量
     * @return 存储文件全路径
     */
    public static final String uploadFileFromURL(String baseDir, String extension, String url, long maxFileSize, String[] allowedExtension) {
        checkExtension(extension, allowedExtension);
        //编码文件名
        String fileName = DateFormatUtils.format(new Date(), "yyyy-MM-dd-HHmmss-") + RandomStringUtils.randomAlphabetic(4) + extension;
        File desc = new File(baseDir + File.separator + fileName);
        BufferedOutputStream bufferedOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(desc));
            //从网络下载文件流
            bufferedInputStream = new BufferedInputStream(new URL(url).openStream());
            byte[] buffer = new byte[2048];
            int len;
            long size = 0;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, len);
                size += len;
                if (size > maxFileSize) {
                    bufferedInputStream.close();
                    bufferedOutputStream.close();
                    //删除已下载的文件
                    desc.delete();
                    throw new CustomException("文件大小超出限制，最大为" + maxFileSize / 1024 / 1024 + "M");
                }
            }
            return desc.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferedInputStream) {
                    bufferedInputStream.close();
                }
                if (null != bufferedOutputStream) {
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                //忽略
            }
        }
        return null;
    }

    /**
     * 检查文件后缀是否是符合要求
     *
     * @param extension
     * @param allowedExtension
     */
    public static final void checkExtension(String extension, String[] allowedExtension) {
        if (null != allowedExtension) {
            List<String> extensionList = new ArrayList<>();
            for (String str : allowedExtension) {
                if (!str.equalsIgnoreCase(extension)) {
                    extensionList.add(str);
                } else {
                    break;
                }
            }
            if (extensionList.size() == allowedExtension.length) {
                throw new CustomException("文件后缀不符合，仅可以为：" + String.join("、", extensionList) + "格式");
            }
        }
    }

    /**
     * 浏览器大文件分片上传/断点续传，前端可由webuploader实现，注意需要是一个文件拆分的分片文件，不同文件合并后可能无法使用
     *
     * @param baseDir      存储文件的基目录
     * @param file         传入的分片文件
     * @param currentChunk 当前分片数
     * @param totalChunk   总分片数
     * @param fileName     存储的文件名称，请保证唯一性
     * @param maxFileSize  分片文件最大大小,单位byte
     * @return 当分片全部上传完成后返回存储文件全路径，否则返回null
     */
    public String splitUploadFileFromWeb(String baseDir, MultipartFile file, int currentChunk, int totalChunk, String fileName, long maxFileSize) throws Exception {
        checkChunk(currentChunk, totalChunk, fileName);
        if (file.getSize() > maxFileSize) {
            throw new CustomException("文件大小超出限制，最大为" + maxFileSize / 1024 / 1024 + "M");
        }
        //取出文件
        String temFileName = currentChunk + "_" + fileName;
        File temFile = new File(baseDir, temFileName);
        temFile.setWritable(true, true);
        if (!temFile.getParentFile().exists()) {
            temFile.getParentFile().mkdirs();
        }
        //断点续传，如果存在则不传，没有则写入临时文件
        if (!temFile.exists()) {
            file.transferTo(temFile);
        }
        //文件合并
        String completeFileName = mergeFile(baseDir, currentChunk, totalChunk, fileName);
        return completeFileName;
    }

    /**
     * 本地大文件分片上传/断点续传，前端可由webuploader实现，注意需要是一个文件拆分的分片文件，不同文件合并后可能无法使用
     * 可以当作是把本地的所有分片合并到一个大文件中
     *
     * @param baseDir      存储文件的基目录
     * @param pathNames    传入的分片文件全路径
     * @param currentChunk 当前分片数
     * @param totalChunk   总分片数
     * @param fileName     存储的文件名称，请保证唯一性
     * @param maxFileSize  分片文件最大大小,单位byte
     * @return 当分片全部上传完成后返回存储文件全路径，否则返回null
     */
    public static String splitUploadFileFromFile(String baseDir, String pathNames, int currentChunk, int totalChunk, String fileName, long maxFileSize) {
        checkChunk(currentChunk, totalChunk, fileName);
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            //取出文件
            String temFileName = currentChunk + "_" + fileName;
            File temFile = new File(baseDir, temFileName);
            temFile.setWritable(true, true);
            if (!temFile.getParentFile().exists()) {
                temFile.getParentFile().mkdirs();
            }
            //断点续传，如果存在则不传，没有则写入临时文件
            if (!temFile.exists()) {
                bufferedInputStream = new BufferedInputStream(new FileInputStream(pathNames));
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(temFile));
                byte[] buffer = new byte[2048];
                int len;
                long size = 0;
                while ((len = bufferedInputStream.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, len);
                    size += len;
                    if (size > maxFileSize) {
                        bufferedInputStream.close();
                        bufferedOutputStream.close();
                        //删除已下载的文件
                        temFile.delete();
                        throw new CustomException("文件大小超出限制，最大为" + maxFileSize / 1024 / 1024 + "M");
                    }
                }
                bufferedOutputStream.close();
                bufferedInputStream.close();
            }
            //文件合并
            String completeFileName = mergeFile(baseDir, currentChunk, totalChunk, fileName);
            return completeFileName;
        } catch (Exception e) {
            throw new CustomException("分片文件上传失败：" + e.getMessage());
        } finally {
            try {
                if (null != bufferedInputStream) {
                    bufferedInputStream.close();
                }
                if (null != bufferedOutputStream) {
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                //忽略
            }
        }
    }

    /**
     * 检查分片参数是否正确
     *
     * @param currentChunk
     * @param totalChunk
     * @param fileName
     */
    public static void checkChunk(int currentChunk, int totalChunk, String fileName) {
        if (currentChunk < 0 || totalChunk <= 0 || currentChunk > totalChunk) {
            throw new CustomException("分片参数错误");
        }
        if (StringUtils.isBlank(fileName)) {
            throw new CustomException("文件名不能为空");
        }
    }

    /**
     * 合并所有分片文件为完整文件
     *
     * @param baseDir
     * @param currentChunk
     * @param totalChunk
     * @param fileName
     * @return
     */
    public static String mergeFile(String baseDir, int currentChunk, int totalChunk, String fileName) {
        if (currentChunk == totalChunk - 1) {
            BufferedOutputStream bufferedOutputStream = null;
            BufferedInputStream bufferedInputStream = null;
            try {
                //到了最后一个分片，写入文件
                File completeFile = new File(baseDir, fileName);
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(completeFile));
                for (int i = 0; i < totalChunk; i++) {
                    File tempFile = new File(baseDir, i + "_" + fileName);
                    //多线程提交分片文件有可能最后一个文件先到，等一下前面的文件
                    int waitCount = 0;
                    while (!tempFile.exists()) {
                        if (waitCount > 10) {
                            throw new CustomException("文件上传超时");
                        }
                        Thread.sleep(100);
                        waitCount++;
                    }
                    //读取临时文件
                    bufferedInputStream = new BufferedInputStream(new FileInputStream(tempFile));
                    byte[] buffer = new byte[2048];
                    int len;
                    while ((len = bufferedInputStream.read(buffer)) != -1) {
                        bufferedOutputStream.write(buffer, 0, len);
                    }
                    bufferedInputStream.close();
                    //删除临时文件
                    tempFile.delete();
                }
                return completeFile.toString();
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("合并分片文件失败：" + e.getMessage());
            } finally {
                try {
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.close();
                    }
                    if (bufferedInputStream != null) {
                        bufferedInputStream.close();
                    }
                } catch (IOException e) {
                    //忽略
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
/*        long start = System.currentTimeMillis();
        String url = "https://280ba890815715b7.jomoxc.com:15872/fs3c4a7504.a.bdydns.com/2513265565/mda-mkr2678qr8dgn1fm/sc/" +
                "cae_h264_nowatermark/1637925136371591314/mda-mkr2678qr8dgn1fm.mp4?bcevod_channel=searchbox_feed&pt=3&klogid=" +
                "2186247347&max_age=2592000&xauzkey=mngx_e3401c28b4e9d2ecfeab9968089d2be0_1682588190&v_from_s=hkapp-haokan-hnb&abtest&" +
                "r=IiU5BARMHF0xJU1ZIBA6MyslJBdZFVwfaCweFm8ZIjV4Z3pMBgQLFiAvSxEvWzokZTIsESgeAURzHhQYNRU9Ijg8LAYcWQJEdHZDRXdFenF5ZnxBTkcAQ3NuFxMjWSQsOGN7Q08HQUojJhRGJBlnKjplcgIoEEEdKh4JSiofKDc6fCUVGB1SHGopFBVkFTwzIg4mEQ5LAkR%2Fc09HcUFxcWdhYERaTgYXcnVLFSBHeyIpY" +
                "n1EREZRRiMgT08gRX50fDIrRhRQUREiNxUTHRchJiQ%2FKBhKBVYTNSISFS0MFiEvNClSBxIOQ2EiHkpyUjkzd2JrGBgRWhZ6c0tPdEZ" +
                "9cHllelIBH1dPdnFPQHREcXRyaHpMT04LQHN1SEZkFSszLyI5SVEdXx0gKB5KcEVxcXhlekdDQRUBIyolDyEQJ3p7JSEoGhQSXUMoMR8ZIRA" +
                "ndg%3D%3D&cd=0&pd=1&xcid=304d371da3a54f4eab8bb9103665934e&sdk_xcdn=1&xcsched=3430241398&xclogid=8304533779700888457" +
                "&vid=10576083897888824421&f_ver=v1&auth_key=1682503586-0-0-85e541bb32ec30030b4da58b1736cf2c&limit_rate=3670016&logid=2186247347";
        System.out.println(uploadFileFromURL("D:\\", ".mp4", url, 80 * 1024 * 1024, null));
        System.out.println("用时:" + (System.currentTimeMillis() - start) / 1000);*/

        //合并分片文件
        File file = new File("D:\\");
        File[] listFiles = file.listFiles();
        ArrayList<String> arrayList = new ArrayList<>();
        for (File item : listFiles) {
            if (item.getName().matches(".*(?:.mp4)$")) {
                System.out.println(item.toString());
                arrayList.add(item.toString());
            }
        }
        for (int i = 0; i < arrayList.size(); i++) {
            String res = splitUploadFileFromFile("D:\\", arrayList.get(i), i, arrayList.size(), "merge.mp4", 500 * 1024 * 1024);
            if (res != null) {
                System.out.println("合并上传完成，文件路径:" + res);
            }
        }
    }
}
