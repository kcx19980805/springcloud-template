package com.kcx.common.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.alibaba.fastjson.JSON;
import com.kcx.common.utils.html.EscapeUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * XSS过滤请求包装
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper
{
    /**
     * @param request
     */
    public XssHttpServletRequestWrapper(HttpServletRequest request)
    {
        super(request);
    }

    @Override
    public String[] getParameterValues(String name)
    {
        String[] values = super.getParameterValues(name);
        if (values != null)
        {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++)
            {
                // 防xss攻击和过滤前后空格
                escapseValues[i] = EscapeUtil.clean(values[i]).trim();
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException
    {
        // 非json类型，直接返回
        if (!isJsonRequest())
        {
            return super.getInputStream();
        }

        // 为空，直接返回
        String json = IOUtils.toString(super.getInputStream(), "utf-8");
        if (StringUtils.isEmpty(json))
        {
            return super.getInputStream();
        }

        // xss过滤
        json = EscapeUtil.clean(json).trim();
        return handlePageParam(json);
    }

    /**
     * 是否是Json请求
     */
    public boolean isJsonRequest()
    {
        String header = super.getHeader(HttpHeaders.CONTENT_TYPE);
        return MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(header);
    }


    /**
     * 通过json自动排序，防止ReqPageBaseVO的page在limit前面导致setPage异常
     * @param json 请求参数
     * @return
     */
    public static  ServletInputStream handlePageParam(String json) throws UnsupportedEncodingException {
        Map<String, Object> map = JSON.parseObject(json);
        final ByteArrayInputStream bis = new ByteArrayInputStream(JSON.toJSONString(map).getBytes("utf-8"));
        return new ServletInputStream()
        {
            @Override
            public boolean isFinished()
            {
                return true;
            }

            @Override
            public boolean isReady()
            {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener)
            {
            }

            @Override
            public int read() throws IOException
            {
                return bis.read();
            }
        };
    }
}
