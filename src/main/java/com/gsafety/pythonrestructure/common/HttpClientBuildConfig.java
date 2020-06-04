package com.gsafety.pythonrestructure.common;

import com.google.gson.Gson;
import com.gsafety.pythonrestructure.python.model.ResultFromAll;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;


public class HttpClientBuildConfig {

    @Autowired
    private CloseableHttpClient httpClient;
    @Autowired
    private RequestConfig config;
    @Value("${gSearchBaseUrl}")
    private String gSearchBaseUrl;
    @Value("${gEsBaseUrl}")
    private String gEsBaseUrl;

    private  static  Gson gson = new Gson();


    public HttpPost getHttpPost(String url, Map<String,Object> map) throws  Exception{
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(config);
        String jsonString = gson.toJson(map);
        StringEntity s = new StringEntity(jsonString, "utf-8");
        s.setContentType("application/json");
        httpPost.setEntity(s);
        return   httpPost;
    }

    public HttpPost getHttpPost(String url, ResultFromAll.ResBean ResBean) throws  Exception{
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(config);
        String jsonString = gson.toJson(ResBean);
        StringEntity s = new StringEntity(jsonString, "utf-8");
        s.setContentType("application/json");
        httpPost.setEntity(s);
        return   httpPost;
    }

    public HttpPost getHttpPost(String url, String resultJson) throws  Exception{
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(config);
        StringEntity s = new StringEntity(resultJson, "utf-8");
        s.setContentType("application/json");
        httpPost.setEntity(s);
        return   httpPost;
    }





}
