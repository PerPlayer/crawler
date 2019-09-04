package com.crawler.util.http;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Map;

public class HttpUtil {

    private static PoolingHttpClientConnectionManager cm = null;

    static{
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(50);
        cm.setDefaultMaxPerRoute(20);
    }

    public static CloseableHttpClient client(){
        RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .setConnectTimeout(30*1000)
                .build();
        CloseableHttpClient client = HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(requestConfig)
                .build();
        return client;
    }

    public static HttpGet get(String url) {
        HttpGet get = new HttpGet(url);
        setHeaders(get);
        return get;
    }

    public static HttpPost post(String url, Map<String, String> params){
        HttpPost post = new HttpPost(url);
        setHeaders(post);
        if (MapUtils.isEmpty(params)) {
            return post;
        }
        ArrayList<NameValuePair> pairs = new ArrayList<>();
        params.forEach((key, value)->{
            pairs.add(new BasicNameValuePair(key, value));
        });
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairs, Consts.UTF_8);
        post.setEntity(formEntity);
        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
        return post;
    }

    public static HttpPost postBody(String url, String body){
        HttpPost post = new HttpPost(url);
        setHeaders(post);
        if (StringUtils.isEmpty(body)) {
            return post;
        }
        StringEntity stringEntity = new StringEntity(body, Consts.UTF_8);
        post.setEntity(stringEntity);
        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        return post;
    }

    public static void setHeaders(HttpRequestBase requestBase){
        requestBase.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        requestBase.setHeader(HttpHeaders.CONTENT_TYPE, "charset=UTF-8");
    }
}
