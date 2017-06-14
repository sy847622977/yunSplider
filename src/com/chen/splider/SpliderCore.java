package com.chen.splider;


import com.chen.exception.GetReponseObjExceoption;
import com.chen.exception.NetStateNotOKException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by chen on 2017/6/14.
 * 进行爬取的主要类
 */
public class SpliderCore {
    private HttpClient httpClient;//爬取的客户端
    private String encoding;//解析时的编码

    /**
     * 爬取构造
     */
    public SpliderCore() {
        httpClient = new DefaultHttpClient();
        encoding = "utf-8";
    }

    /**
     * 爬取构造
     * @param encoding 编码
     */
    public SpliderCore(String encoding) {
        httpClient = new DefaultHttpClient();
        this.encoding = encoding;
    }

    /**
     * 爬取构造
     * @param httpClient 客户端
     * @param encoding 编码
     */
    public SpliderCore(HttpClient httpClient, String encoding) {
        this.httpClient = httpClient;
        this.encoding = encoding;
    }

    /**
     * 以get凡是进行爬取
     * @param url 爬取的链接
     * @return 爬取的String类型结果
     * @throws NetStateNotOKException response的状态码不是ok时的异常
     * @throws GetReponseObjExceoption 获取response失败
     */
    public String doGet(String url) throws NetStateNotOKException, GetReponseObjExceoption {
        return doGet(url, null);
    }

    public String doGet(String url, Map<String, String> requestHeadMap)
            throws NetStateNotOKException, GetReponseObjExceoption {
        String result = "";
        HttpGet request = new HttpGet(url);//创建get请求对象

        //设置请求头
        if (requestHeadMap != null && !requestHeadMap.isEmpty()) {
            Set<String> keySet = requestHeadMap.keySet();
            for (String s : keySet) {
                request.setHeader(s, requestHeadMap.get(s));
            }
        }

        // 通过请求对象获取响应对象
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
            throw new GetReponseObjExceoption(url);
        }


        // 判断网络连接状态码是否正常(0--200都数正常)
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, encoding);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new NetStateNotOKException(url, statusLine.getStatusCode());
            }
        }
        return result;
    }


    /**
     * 获取编码
     * @return 编码
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * 设置编码
     * @param encoding 编码
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}