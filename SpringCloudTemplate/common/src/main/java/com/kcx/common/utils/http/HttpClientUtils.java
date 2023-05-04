package com.kcx.common.utils.http;


import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

/**
 * 网络请求工具类
 * CloseableHttpClient是一个连接池，内部调用HttpClient执行请求，能够即时释放资源
 * RestTemplate内部也是使用CloseableHttpClient，如果RestTemplate不能满足要求则用此工具类发起请求
 */
@Slf4j
public class HttpClientUtils {
    //发送请求的客户端单例
    private static CloseableHttpClient httpClient;
    //线程锁
    private final static Object lock = new Object();

    /***
     * 发送post请求
     * @param url 请求地址
     * @param param json转string类型的参数
     * @return
     */
    public static String executePost(String url, String param) {
        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/json;charset=UTF-8");
        post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
        post.addHeader("Accept", "application/json");
        post.setEntity(new StringEntity(param, "UTF-8"));
        return executePost(post);
    }

    /**
     * 发送post请求
     *
     * @param post 设置了请求地址和请求头参数的HttpPost对象
     * @return 响应结果，可以自己转json
     */
    public static String executePost(HttpPost post) {
        return executeGetAndPost(post);
    }

    /***
     * 发送get请求
     * @param url 拼接好参数的请求地址
     * @return
     */
    public static String executeGet(String url) {
        HttpGet get = new HttpGet(url);
        get.addHeader("Content-Type", "application/json;charset=UTF-8");
        get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
        get.addHeader("Accept", "application/json");
        return executeGet(get);
    }

    /**
     * 发送get请求
     *
     * @param get 设置了请求地址和请求头参数的HttpGet对象
     * @return 响应结果，可以自己转json
     */
    public static String executeGet(HttpGet get) {
        return executeGetAndPost(get);
    }

    /**
     * 发送请求
     * @param httpRequestBase 请求对象
     * @return 响应结果，可以自己转json
     */
    public static String executeGetAndPost(HttpRequestBase httpRequestBase) {
        CloseableHttpClient httpClient = getHttpClient();
        try {
            CloseableHttpResponse response = httpClient.execute(httpRequestBase);
            int statusCode = response.getStatusLine().getStatusCode();
            //如果网址响应状态码不为200和204，则是异常
            if (statusCode != HttpServletResponse.SC_OK && statusCode != HttpServletResponse.SC_NO_CONTENT) {
                throw new HttpException("请求异常，返回结果："+EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
            }
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //主动释放连接
            httpRequestBase.releaseConnection();
        }
        return null;
    }

    /**
     * 获取请求客户端对象
     * CloseableHttpClient本身就是一个连接池，所以应该获取单例
     * 双重检查实现单例模式
     *
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            //多线程下多个线程同时调用getHttpClient容易导致重复创建httpClient对象的问题,所以加上了同步锁
            synchronized (lock) {
                if (httpClient == null) {
                    //设置请求超时时间10秒
                    RequestConfig requestConfig = RequestConfig.custom()
                            .setConnectionRequestTimeout(10000)
                            .setConnectTimeout(10000)
                            .setSocketTimeout(10000)
                            .build();
                    //连接池管理
                    PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(getVerifySSLRegistry());
                    //连接池最大并发连接数,设置过小并发请求会串行执行，此时后面被阻塞的请求可能会抛出请求超时异常
                    manager.setMaxTotal(3000);
                    //单域名下最大连接数
                    manager.setDefaultMaxPerRoute(400);
                    //请求失败时,进行请求重试的处理
                    HttpRequestRetryHandler handler = (e, i, httpContext) -> {
                        if (i > 3) {
                            log.error("http请求重试超过3次,放弃请求");
                            return false;
                        }
                        if (e instanceof NoHttpResponseException) {
                            log.error("CloseableHttpClient请求未收到来自服务器的响应,原因：{},开始进行重试", e.getMessage());
                            return true;
                        }
                        if (e instanceof SSLHandshakeException) {
                            log.error("CloseableHttpClient请求SSL握手异常:{}", e.getMessage());
                            return false;
                        }
                        if (e instanceof InterruptedIOException) {
                            log.error("CloseableHttpClient请求超时被打断:{}", e.getMessage());
                            return false;
                        }
                        if (e instanceof UnknownHostException) {
                            log.error("CloseableHttpClient请求未知服务器主机:{}", e.getMessage());
                            return false;
                        }
                        if (e instanceof SSLException) {
                            log.error("CloseableHttpClient请求SSL异常:{}", e.getMessage());
                            return false;
                        }
                        HttpClientContext context = HttpClientContext.adapt(httpContext);
                        HttpRequest request = context.getRequest();
                        //如果请求不是关闭连接的请求返回true，否则false
                        return !(request instanceof HttpEntityEnclosingRequest);
                    };
                    httpClient = HttpClients.custom()
                            .setDefaultRequestConfig(requestConfig)
                            //指定连接池的管理方式
                            .setConnectionManager(manager)
                            //使HttpClient的此实例使用后台线程主动从连接池中逐出过期的连接。
                            .evictExpiredConnections()
                            //使HttpClient的此实例使用后台线程主动从连接池中逐出空闲30秒连接。
                            .evictIdleConnections(30, TimeUnit.SECONDS)
                            //设置重试处理逻辑
                            .setRetryHandler(handler)
                            .build();
                }
            }
        }
        return httpClient;
    }

    /**
     * 一个安全验证请求地址的注册器
     *
     * @return
     */
    public static Registry getVerifySSLRegistry() {
        SSLContext sslContext = null;
        try {
            sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, (X509Certificate[] chain, String authType) -> true)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, null,
                null, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", csf)
                .build();
        return registry;
    }

    /**
     * 一个绕过SSL安全验证请求地址的注册器
     *
     * @return
     */
    public static Registry getIgnoreVerifySSLRegistry() {
        LayeredConnectionSocketFactory sslSocketFactory = null;
        //绕过SSL验证证书没用了，对https不进行安全验证
        try {
            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sc.init(null, new TrustManager[]{trustManager}, null);
            sslSocketFactory = new SSLConnectionSocketFactory(sc);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error("创建CloseableHttpClient请求对象异常，原因：{}", e.getMessage());
        }
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory).build();
        return registry;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //使用CloseableHttpClient
/*                    CloseableHttpClient httpClient = getHttpClient();
                    HttpGet get = new HttpGet("http://127.0.0.1:8080/user/login?username=123&password=123456");
                    try {
                        CloseableHttpResponse response = httpClient.execute(get);
                        System.err.println(EntityUtils.toString(response.getEntity()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "-----" + httpClient.hashCode());*/

                    //使用RestTemplate
/*                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> res = restTemplate.getForEntity("http://127.0.0.1:8080/user/login?username=123&password=123456",String.class);
                    System.out.println("------------------------------------------");
                    System.out.println(res.getBody());*/

                   // System.err.println(executeGet("https://zhuanlan.zhihu.com/api/articles/524774467/recommendation?include=data%5B*%5D.article.column&limit=12&offset=0"));
/*                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("username","123");
                    jsonObject.put("password","123456");
                    System.err.println(executePost("http://127.0.0.1:8080/user/login",jsonObject.toString()));*/
                }
            }, "线程" + i).start();
        }
    }
}
