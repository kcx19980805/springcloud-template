package com.kcx.common.utils.ip;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取IP方法
 */
@Slf4j
public class IpUtils {
    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
     * @param request 浏览器请求对象
     * @return 192.168.233.1
     */
    public static final String getIpAddress(HttpServletRequest request) {
            String ipAddress = null;
            try {
                //X-Forwarded-For：一个 HTTP 扩展头部，主要是为了让 Web 服务器获取访问用户的真实 IP 地址。每个 IP 地址，
                // 每个值通过逗号+空格分开，最左边是最原始客户端的 IP 地址，中间如果有多层代理，每⼀层代理会将连接它的客户端 IP
                // 追加在 X-Forwarded-For 右边。
                ipAddress = request.getHeader("X-Forwarded-For");
                if (ipAddress != null && ipAddress.length() != 0 && !"unknown".equalsIgnoreCase(ipAddress)) {
                    // 多次反向代理后会有多个ip值，第一个ip才是真实ip
                    if (ipAddress.indexOf(",") != -1) {
                        ipAddress = ipAddress.split(",")[0];
                    }
                }
                if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                    //这个一般是经过 Apache http 服务器的请求才会有，用 Apache http 做代理时一般会加上 Proxy-Client-IP 请求头
                    ipAddress = request.getHeader("Proxy-Client-IP");
                }
                if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                    //也是通过 Apache http 服务器，在 weblogic 插件加上的头。
                    ipAddress = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                    //代理服务器发送的HTTP头。如果是“超级匿名代理”，则返回none值。
                    ipAddress = request.getHeader("HTTP_CLIENT_IP");
                }
                if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                    //X-Real-IP一般只记录真实发出请求的客户端IP
                    ipAddress = request.getRemoteAddr();
                }
            }catch (Exception e) {
                e.printStackTrace();
                log.error("获取ip地址异常： ",e.getMessage());
            }
            return ipAddress;
    }

    /**
     * 根据ip地址获取归属地
     * @param ip
     * @return 国内：中国|四川省|成都市|移动 国外：美国|犹他|盐湖城
     */
    public static String getCityInfoByIp(String ip) {
        //获得文件流时，因为读取的文件是在打好jar文件里面，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
        InputStream is = null;
        DbSearcher searcher = null;
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("ip2region.db");
            Resource resource = resources[0];
            is = resource.getInputStream();
            File target = new File("ip2region.db");
            FileUtils.copyInputStreamToFile(is, target);
            is.close();
            if (StringUtils.isEmpty(String.valueOf(target))) {
                log.error("加载ip2region.db文件失败");
                return null;
            }
            DbConfig config = new DbConfig();
            searcher = new DbSearcher(config, String.valueOf(target));
        }catch (Exception e){
            e.printStackTrace();
            log.error("加载ip2region.db文件失败，原因：{}",e.getMessage());
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    //忽略
                }
            }
        }
        try {
            Method method;
            method = searcher.getClass().getMethod("btreeSearch", String.class);
            DataBlock dataBlock;
            if (!Util.isIpAddress(ip)) {
                log.error("ip地址无效");
            }else{
                dataBlock = (DataBlock) method.invoke(searcher, ip);
                String ipInfo = dataBlock.getRegion();
                if (!StringUtils.isEmpty(ipInfo)) {
                    ipInfo = ipInfo.replace("|0", "");
                    ipInfo = ipInfo.replace("0|", "");
                }
                return ipInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据ip地址获取归属地,个性化
     * ip 属地在国内的话，只会展示省份，而国外的话，只会展示国家.
     * @param ip 国内：四川省 国外：美国
     * @return
     */
    public static String getPossessionCityInfoByIp(String ip){
        String cityInfo = IpUtils.getCityInfoByIp(ip);
        if (!StringUtils.isEmpty(cityInfo)) {
            cityInfo = cityInfo.replace("|", " ");
            String[] cityList = cityInfo.split(" ");
            if (cityList.length > 0) {
                // 国内的显示到具体的省
                if ("中国".equals(cityList[0])) {
                    if (cityList.length > 1) {
                        return cityList[1];
                    }
                }
                // 国外显示到国家
                return cityList[0];
            }
        }
        return "未知";
    }

    /**
     * 获取当前机器ip地址
     * @return 192.168.233.1
     */
    public static final String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
        }
        return "未知";
    }

    /**
     * 获取当前机器名称
     * @return DESKTOP-G10U45Q
     */
    public static final String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
        }
        return "未知";
    }

    /**
     * 要测试先注释掉包含HttpServletRequest的方法
     */
    public static void main(String[] args) {
        //国内ip
/*        String ip1 = "117.175.49.231";

        String cityInfo1 = IpUtils.getCityInfoByIp(ip1);
        System.out.println(cityInfo1);
        String address1 = IpUtils.getPossessionCityInfoByIp(ip1);
        System.out.println(address1);

        //国外ip
        String ip2 = "67.220.90.13";
        String cityInfo2 = IpUtils.getCityInfoByIp(ip2);
        System.out.println(cityInfo2);
        String address2 = IpUtils.getPossessionCityInfoByIp(ip2);
        System.out.println(address2);*/
    }
}
