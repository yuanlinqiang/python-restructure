package com.gsafety.pythonrestructure.python.controller;

import com.google.gson.Gson;
import com.gsafety.pythonrestructure.Utils.StringCommentUtils;
import com.gsafety.pythonrestructure.common.ResultMsg;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by YLQ on 2017/9/28.
 */
@RestController
@RequestMapping("/python/api")
public class GSearchController {

    @Autowired
    private CloseableHttpClient httpClient;
    @Autowired
    private RequestConfig config;

    @Value("${gSearchBaseUrl}")
    private String gSearchBaseUrl;


    /**
     * 搜索问句解析接口
     * */
    @RequestMapping(value = "/v1/query_recognition", method = RequestMethod.POST)
    public String addUser(@RequestBody String  Params) {
        try {
            //因为接口为直接覆盖接口，所以需要把已有用户先查询出来
            String url = gSearchBaseUrl + "/search/query_recognition";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(config);
            Gson gson = new Gson();
            httpPost.setConfig(config);
            //获取Token
          //  String jsonString = gson.toJson(Params);
            StringEntity s = new StringEntity(Params, "utf-8");
            s.setContentType("application/json");//
            httpPost.setEntity(s);
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            Map map = gson.fromJson(jsonStr, Map.class);
            Map<String,Object> res = StringCommentUtils.get(Map.class, map.get("res"));
            res.put("query_data",res.get("query"));
            res.remove("query");
            List<String> intents = StringCommentUtils.get(List.class, res.get("intent"));
            if(intents.size() == 0 ) return ResultMsg.success();
            jsonStr = searchQueryParse(res);
            return   jsonStr;
        } catch (Exception e) {
            return ResultMsg.fails();
        }
    }



    /**
     * 精确搜索   type来源   上面结构传递
     * */
    @RequestMapping(value = "/v1/search/query_parse", method = RequestMethod.POST)
    public String searchQueryParse(@RequestBody Map<String,Object>  map) {
        Gson gson = new Gson();
        String jsonStr;
        try {
            //因为接口为直接覆盖接口，所以需要把已有用户先查询出来
            String url = gSearchBaseUrl + "/search/query_parse" + "?type="+map.get("type");
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(config);
            httpPost.setConfig(config);
            String result = gson.toJson(map);
            StringEntity s = new StringEntity(result, "utf-8");
            s.setContentType("application/json");
            httpPost.setEntity(s);
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            return   jsonStr;
        } catch (Exception e) {
            return ResultMsg.fails();
        }
    }




}
