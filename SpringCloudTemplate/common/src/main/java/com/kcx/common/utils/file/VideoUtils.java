package com.kcx.common.utils.file;
import com.kcx.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频文件工具类
 */
@Slf4j
public class VideoUtils {

    /**
     * 合并多个不同的mp4文件
     * @param videoFileList mp4文件全路径
     * @param ffmpegPath ffmpeg程序的全路径
     * @param targetFilePath mp4文件全路径
     * @return mp4文件全路径
     */
    public static String mergeMp4File(List<String> videoFileList, String ffmpegPath,String targetFilePath){
        List<String> tsFileList = convertMp4ToTsFile(videoFileList,ffmpegPath,targetFilePath);
        return mergeTsToMp4File(tsFileList,ffmpegPath,targetFilePath);
    }

    /**
     * 将mp4文件转为ts文件,1个mp4对应1个ts文件
     * @param videoFileList mp4文件全路径
     * @param ffmpegPath ffmpeg程序的全路径
     * @param targetFilePath mp4文件全路径
     * @return ts文件路径，ts的文件名称就是源mp4文件名称，只是后缀改为.ts
     */
    public static List<String> convertMp4ToTsFile(List<String> videoFileList, String ffmpegPath,String targetFilePath){
        if(null == videoFileList || videoFileList.size() <= 0){
            throw new CustomException("mp4文件不能为空");
        }
        List<String> tsFileList = new ArrayList<>();
        Process process;
        List<String> command;
        for (String videoFile : videoFileList) {
            String tsFileName = videoFile.substring(0, videoFile.lastIndexOf(".")) + ".ts";
            command = new ArrayList<>();
            command.add(ffmpegPath);
            command.add("-y");
            command.add("-i");
            command.add(videoFile);
            command.add("-vcodec");
            command.add("copy");
            command.add("-bsf:v");
            command.add("h264_mp4toannexb");
            command.add("-f");
            command.add("mpegts");
            command.add(tsFileName);
            log.info("执行mp4转换为ts文件命令："+String.join(" ",command));
            tsFileList.add(tsFileName);
            try {
                //开始执行命令
                process = Runtime.getRuntime().exec(String.join(" ",command));
                process.getOutputStream().close();
                process.getInputStream().close();
                process.getErrorStream().close();
                try {
                    process.waitFor();
                    process.destroy();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //输出执行过程
/*                InputStream errorStream = process.getErrorStream();
                InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
                BufferedReader br = new BufferedReader(inputStreamReader);
                String line = "";
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                System.out.println(sb);
                */
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tsFileList;
    }

    /**
     * 合并所有ts文件到一个mp4文件
     * @param tsFileList ts文件全路径
     * @param ffmpegPath ffmpeg程序的全路径
     * @param targetFilePath mp4文件全路径
     * @return mp4文件全路径
     */
    public static String mergeTsToMp4File(List<String> tsFileList, String ffmpegPath,String targetFilePath){
        if(null == tsFileList || tsFileList.size() <= 0){
            throw new CustomException("ts文件不能为空");
        }
        StringBuffer command = new StringBuffer();
        command.append(ffmpegPath);
        command.append(" -i ");
        command.append("concat:");
        for (int i = 0; i < tsFileList.size(); i++) {
            if (i != tsFileList.size() - 1) {
                command.append(tsFileList.get(i) + "|");
            } else {
                command.append(tsFileList.get(i));
            }
        }
        command.append(" -vcodec ");
        command.append(" copy ");
        command.append(" -bsf:a ");
        command.append(" aac_adtstoasc ");
        command.append(" -movflags ");
        command.append(" +faststart ");
        command.append(targetFilePath);
        String execCommand = String.join(" ",command);
        log.info("执行ts合并为mp4命令："+execCommand);
        Process process = null;
        try {
            //执行合并文件命令
            process = Runtime.getRuntime().exec(execCommand);
            process.getOutputStream().close();
            process.getInputStream().close();
            process.getErrorStream().close();
            try {
                process.waitFor();
                process.destroy();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //删除生成的ts文件
            for (String filePath : tsFileList) {
                File file = new File(filePath);
                file.delete();
                process.destroy();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return targetFilePath;
    }

    /**
     * 将1个mp4文件拆分为多个ts视频文件，前端可以通过hls.js播放每次只请求一个ts，提高播放速度
     * @param videoFilePath 视频文件全路径
     * @param hlsTime 每个分片时长，默认2，单位秒，建议10
     * @param maxTsCount 生成ts文件最大数量，可以为null，使文件数量尽量接近
     * @param ffmpegPath ffmpeg程序的全路径
     * @param baseDir 生成的m3u8索引文件和生成的ts文件存放的基目录
     * @param m3u8FileName  生成的m3u8索引文件名称
     * @param tsFilePrefix ts文件名的前缀，不能包含,ts文件名称为tsFilePrefix0.ts,tsFilePrefix1.ts...
     * @return
     */
    public static int convertMp4ToM3u8(String videoFilePath,int hlsTime,Integer maxTsCount, String ffmpegPath,String baseDir,String m3u8FileName,String tsFilePrefix){
        int tsCount;
        //提前计算最后生成的ts文件数量
        float videoTime = getVideoTime(videoFilePath,"seconds");
        tsCount=(int)(videoTime/hlsTime)+(videoTime%hlsTime>0?1:0);
        String m3u8FilePath = baseDir+m3u8FileName;
        if(null != maxTsCount && maxTsCount>0){
            if(maxTsCount > 1000 ){
                throw new CustomException("ts文件数量不能大于1000");
            }
            if(tsCount > maxTsCount){
                if(maxTsCount > videoTime){
                    hlsTime = 2;
                }else{
                    //重新计算每个分片的时长
                    hlsTime = (int)videoTime/maxTsCount;
                }
                tsCount =(int)(videoTime/hlsTime)+(videoTime%hlsTime>0?1:0);
            }
        }
        Process process;
        List<String> command;
        command = new ArrayList<>();
        command.add(ffmpegPath);
        command.add("-y");
        command.add("-i");
        command.add(videoFilePath);
        //解决设置hls_time切片时间不准确,linux下无法识别
        //command.add("-force_key_frames \"expr:gte(t,n_forced*"+hlsTime+")\"");
        command.add("-hls_time "+hlsTime);
        command.add("-hls_playlist_type");
        command.add("vod");
        command.add("-hls_segment_filename");
        command.add(baseDir+tsFilePrefix+"%d.ts");
        command.add(m3u8FilePath);
        String execCommand = String.join(" ",command);
        log.info("执行mp4转换为ts文件命令："+execCommand);
        try {
            //开始执行命令
            process = Runtime.getRuntime().exec(String.join(" ",execCommand));
            process.getOutputStream().close();
            process.getInputStream().close();
            process.getErrorStream().close();
            try {
                process.waitFor();
                process.destroy();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tsCount;
    }

    /**
     * 获取视频时长
     * @param filePath 视频文件全路径
     * @param unit 返回时长单位，seconds秒,minutes分钟,hour小时,默认返回毫秒
     * @return
     */
    public static Float getVideoTime(String filePath,String unit) {
        try {
            MultimediaObject instance = new MultimediaObject(new File(filePath));
            MultimediaInfo result = instance.getInfo();
            // 毫秒 -> 秒 -> 分钟 -> 小时
            Float ls = result.getDuration() / 1000.0f;
            switch (unit){
                case "seconds":
                    return result.getDuration() / 1000.0f;
                case "minutes":
                    return result.getDuration() / 1000.0f / 60.0f ;
                case "hour":
                    return result.getDuration() / 1000.0f / 60.0f / 60.0f ;
                default:
                    return Float.valueOf(result.getDuration());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    public static void main(String[] args) {
        //合并多个mp4文件
/*        File file = new File("D:\\");
        File[] listFiles = file.listFiles();
        ArrayList<String> arrayList = new ArrayList<>();
        for (File item : listFiles) {
            if (item.getName().matches(".*(?:.mp4)$")) {
                System.out.println(item.toString());
                arrayList.add(item.toString());
            }
        }
        mergeMp4File(arrayList,"D:\\视频无损合并工具\\ffmpeg.exe","D:\\merge.mp4");*/


        //mp4转ts
/*        File file = new File("D:\\");
        File[] listFiles = file.listFiles();
        ArrayList<String> arrayList = new ArrayList<>();
        for (File item : listFiles) {
            if (item.getName().matches(".*(?:.mp4)$")) {
                System.out.println(item.toString());
                arrayList.add(item.toString());
            }
        }
        convertMp4ToTsFile(arrayList,"D:\\视频无损合并工具\\ffmpeg.exe","tsMerge1.mp4");*/

        //ts转mp4
        File file2 = new File("D:\\");
        File[] listFiles2 = file2.listFiles();
        ArrayList<String> arrayList2 = new ArrayList<>();
        for (File item : listFiles2) {
            if (item.isFile() && item.getName().matches(".*(?:.ts)$")) {
                System.out.println(item.toString());
                arrayList2.add(item.toString());
            }
        }
        //mergeTsToMp4File(arrayList2, "D:\\视频无损合并工具\\ffmpeg.exe","D:\\tsMerge.mp4");

        //mp4切片为ts
       // System.out.println(convertMp4ToM3u8("D:\\本草纲目.mp4",50,400,"D:\\视频无损合并工具\\ffmpeg.exe","D:\\","playlist.m3u8","file"));
    }
}
