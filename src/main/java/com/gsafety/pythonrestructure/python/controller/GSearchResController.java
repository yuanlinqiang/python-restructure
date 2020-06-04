package com.gsafety.pythonrestructure.python.controller;

import com.google.gson.Gson;
import com.gsafety.pythonrestructure.Utils.PageHelp;
import com.gsafety.pythonrestructure.Utils.StringCommentUtils;
import com.gsafety.pythonrestructure.common.HttpClientBuildConfig;
import com.gsafety.pythonrestructure.common.ResultMsg;
import com.gsafety.pythonrestructure.python.model.ResultFromAll;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/python/api")
public class GSearchResController {


    @Autowired
    private CloseableHttpClient httpClient;

    private HttpClientBuildConfig httpClientBuildConfig =  new  HttpClientBuildConfig();
    @Value("${gSearchBaseUrl}")
    private String gSearchBaseUrl;
    @Value("${gQueryBaseUrl}")
    private String gQueryBaseUrl;

    private  static  Gson gson = new Gson();

    String  searchIntentResult = ""; //问句意图识别
    String  searchMentionLink = "";  //职称要素识别
    String  queryType; //专家、队伍


    /**
     * Query模版、槽位择优    将查询1 2 封装的对象传递过来
     * */
    @RequestMapping(value = "/v2/search_intention_element", method = RequestMethod.POST)
    public String searchIntentAndElement(@RequestBody Map<String,Object>  map) {

        Thread threadOne = null;

        try {
            threadOne = new Thread(new Runnable() {
                public void run() {
                    searchIntent(map);
                }
            });
            Thread threadTwo = new Thread(new Runnable() {
                public void run() {
                    searchMentionLink(map);
                }
            });
            // 执行线程
            threadOne.start();
            threadTwo.start();
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }


        ResultFromAll intentResult = gson.fromJson(searchIntentResult, ResultFromAll.class);
        ResultFromAll mentionLink = gson.fromJson(searchMentionLink, ResultFromAll.class);
        mentionLink.getRes().setLabel_prob(intentResult.getRes().getLabel_prob());
        mentionLink.getRes().setLabel_type_index(intentResult.getRes().getLabel_type_index());
        mentionLink.getRes().setProcessed_query(intentResult.getRes().getProcessed_query());
        mentionLink.getRes().setRaw_query(map.get("query_data").toString());
        String intentTemplate = searchIntentTemp(mentionLink);
        return  intentTemplate;
    }


    /**
     * 问句意图
     * */
    @RequestMapping(value = "/v2/search_query_intent_cls", method = RequestMethod.POST)
    public void searchIntent(@RequestBody Map<String,Object>  map)  {
        try {
            //因为接口为直接覆盖接口，所以需要把已有用户先查询出来
            String url = gSearchBaseUrl + "/search/v2/search_query_intent_cls";
            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, map);
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (response.getStatusLine().getStatusCode() == 200) {
                searchIntentResult = jsonStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 职称要素识别
     * */
    @RequestMapping(value = "/v2/search_query_mention_link", method = RequestMethod.POST)
    public void searchMentionLink(@RequestBody Map<String,Object>  map) {
        try {
            //因为接口为直接覆盖接口，所以需要把已有用户先查询出来
            String url = gSearchBaseUrl + "/search/v2/search_query_mention_link";
            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, map);
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (response.getStatusLine().getStatusCode() == 200) {
                searchMentionLink = jsonStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String  searchMentionLinkResult(@RequestBody Map<String,Object>  map) {
        String jsonStr = "";
        try {
            //因为接口为直接覆盖接口，所以需要把已有用户先查询出来
            String url = gSearchBaseUrl + "/search/v2/search_query_mention_link";
            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, map);
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonStr;
    }


    /**
     * Query模版、槽位择优    将查询1 2 封装的对象传递过来
     * */
    @RequestMapping(value = "/v2/intent_template_slot_opt", method = RequestMethod.POST)
    public String  searchIntentTemp(@RequestBody ResultFromAll  resultFromAll) {
        try {
            //因为接口为直接覆盖接口，所以需要把已有用户先查询出来
            String url = gSearchBaseUrl + "/search/v3/intent_template_slot_opt";
            ResultFromAll.ResBean res = resultFromAll.getRes(); //请求对象
            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, res);
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (response.getStatusLine().getStatusCode() == 200) {
                return  jsonStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ResultMsg.success();
    }



    /**
     * Query查询   构建body体   将三的结果封装起来
     * */
    @RequestMapping(value = "/v2/query_request_builder", method = RequestMethod.POST)
    public String  searchResBuilder(@RequestBody Map<String,Object> map) {
        try {
            String jsonResult = searchIntentAndElement(map);
            Map resultMap = gson.fromJson(jsonResult, Map.class);  //里面存在Java的公共class类
            Map<String,Object> reqMap = StringCommentUtils.get(Map.class, resultMap.get("res"));
            queryType = reqMap.get("type").toString();
            //因为接口为直接覆盖接口，所以需要把已有用户先查询出来
            String url = gSearchBaseUrl + "/search/v2/query_request_builder";
            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, gson.toJson(reqMap));
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (response.getStatusLine().getStatusCode() == 200) {
                return  jsonStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ResultMsg.success();
    }



    //==================================================魏宁数据接口======================================


    /**
     * Query查询body生成   总入口  将查询、推荐、通用三个接口的数据统一返给前端
     * */
    @RequestMapping(value = "/v2/query_recommend_general_result", method = RequestMethod.POST)
    public String searchCommendGeneralResult(@RequestBody Map<String,Object> map) {

        Map paramsMap = new HashMap();
        paramsMap.put("query_data",map.get("query_data").toString());

        String  searchIntentAndElement = searchIntentAndElement(paramsMap); //第三部的数据结果
        Map resultMap = gson.fromJson(searchIntentAndElement, Map.class);  //Query槽的查询内容  需要传递给前端
        Map<String,Object> reqMap = StringCommentUtils.get(Map.class, resultMap.get("res"));

        //查询
        Map  searchMap = new HashMap();
        searchMap.put("optimal_res",reqMap);
        searchMap.put("page_index",map.get("page_index").toString());
        searchMap.put("page_size",map.get("page_size").toString());

        String searchResult = searchResult(searchMap);
        Map searchResultMap = gson.fromJson(searchResult, Map.class);
        searchResultMap = StringCommentUtils.get(Map.class,searchResultMap.get("res"));


        //推荐
        searchMap.put("page_index",map.get("page_index").toString());
        searchMap.put("page_size",map.get("page_size").toString());
        String recommendResult = recommendResult(searchMap);
        Map recommendResultMap = gson.fromJson(recommendResult, Map.class);
        recommendResultMap = StringCommentUtils.get(Map.class,recommendResultMap.get("res"));

        // 通用
        //将2的查询结果返回给前端
        String searchMentionLinkResult = searchMentionLinkResult(paramsMap);
        Map searchMentionLinkResultMap = gson.fromJson(searchMentionLinkResult, Map.class);
        searchMentionLinkResultMap = StringCommentUtils.get(Map.class,searchMentionLinkResultMap.get("res"));


        Map<String,Object>  generalMap = new HashMap<>();
        generalMap.put("analysis_res",searchMentionLinkResultMap);
        generalMap.put("page_num",map.get("page_index").toString());
        generalMap.put("page_size",map.get("page_size").toString());
        generalMap.put("query_data",map.get("query_data").toString());



        String generalResult = generalResult(generalMap);
        Map generalResultMap = gson.fromJson(generalResult, Map.class);
        generalResultMap = StringCommentUtils.get(Map.class,generalResultMap.get("res"));
        Object generalResultMap1 = generalResultMap.get("general_res");

        //数据封装
        Map<String,Object> mapSearchRes = new HashMap<>();
        mapSearchRes.put("search_res",gson.fromJson(gson.toJson(searchResultMap.get("search_res")), Map.class));  //查询类结果
        List   list = new ArrayList();
        mapSearchRes.put("recommend_res", recommendResultMap ==null ?list : gson.fromJson(gson.toJson(recommendResultMap.get("recommend_res")), Map.class));  //查询类结果
        mapSearchRes.put("general_res",gson.fromJson(generalResultMap == null? null: gson.toJson(generalResultMap.get("general_res")), Map.class));  //查询类结果
        mapSearchRes.put("analysis_res",searchMentionLinkResultMap);
        mapSearchRes.put("optimal_res",reqMap);


        return ResultMsg.customizeSuccess(mapSearchRes);
    }



    /**
     * 查询类结果数据  只有分页信息以及第四步骤生成的Body体
     * */

    @RequestMapping(value = "/v2/query_result", method = RequestMethod.POST)
    public String searchResult(@RequestBody Map<String,Object> map) {

        int pageIndex = 1;
        int pageSize = 10;
        if(!"".equals(map.get("page_index").toString())){
            pageIndex = Integer.parseInt(map.get("page_index").toString());
            pageSize = Integer.parseInt(map.get("page_size").toString());
        }

        map.remove("page_index");
        map.remove("page_size");
        //拿到3的Body体
        map = StringCommentUtils.get(Map.class, map.get("optimal_res"));
        String queryType = map.get("type").toString();
        if("expert".equals(queryType)){
            queryType = "专家";
        }else if("rescue_team".equals(queryType)){
            queryType = "队伍";
        }else if("equipment".equals(queryType)){
            queryType = "装备";
        }else if("hazardous_chemical".equals(queryType)){
            queryType = "危化品";
        }
        //处理小数点后面有0的情况
        String  classType123 = map.get("class").toString();// 前端展示分类
        if(queryType.equals("专家") && "".equals(classType123)){  //处理小数点问题
            List slot = StringCommentUtils.get(List.class, map.get("slot"));
            List<Map<String,Object>> soltList  = new ArrayList();
            for (int i = 0; i < slot.size(); i++) {
                Map  map12  = StringCommentUtils.get(Map.class,slot.get(0));
                List person = StringCommentUtils.get(List.class, map12.get("person"));
                if(person == null){
                    Map<String,Object> mapNew = new HashMap<>();
                    for (int j = 0; j < person.size(); j++) {
                        String sss = StringCommentUtils.rvZeroAndDot(person.get(j).toString());
                        person.set(j,sss);
                    }
                    mapNew.put("person",person);
                    soltList.add(mapNew);
                };


            }
            map.put("slot",soltList);

        }else  if(queryType.equals("危化品") && "B".equals(classType123)){  //处理小数点问题
            List slot = StringCommentUtils.get(List.class, map.get("slot"));
            List<Map<String,Object>> soltListNew  = new ArrayList();
            for (int i = 0; i < slot.size(); i++) {
                Map  map12  = StringCommentUtils.get(Map.class,slot.get(i));
                List person = StringCommentUtils.get(List.class, map12.get("hazardous_chemical_material_name"));
                Map<String,Object> mapNew = new HashMap<>();
                for (int j = 0; j < person.size(); j++) {
                    String sss = StringCommentUtils.rvZeroAndDot(person.get(j).toString());
                    person.set(j,sss);
                    mapNew.put("hazardous_chemical_material_name",person);
                }
                soltListNew.add(mapNew);
            }
            map.put("slot",soltListNew);
        }

        String url = null;
        String jsonStr = null;
        try {
            url = gSearchBaseUrl + "/search/v2/query_request_builder";
            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, gson.toJson(map));
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }


        Map resultMap = gson.fromJson(jsonStr, Map.class);
        Map<String,Object> reqMap = StringCommentUtils.get(Map.class, resultMap.get("res"));
        Map search_body = StringCommentUtils.get(Map.class, reqMap.get("search_body"));


        String  classType = search_body.get("class").toString();// 前端展示分类
        if(queryType.equals("专家") && "B".equals(classType)){  //处理小数点问题
            List slot = StringCommentUtils.get(List.class, search_body.get("slot"));
            List<Map<String,Object>> soltList  = new ArrayList();
            for (int i = 0; i < slot.size(); i++) {
                Map  map12  = StringCommentUtils.get(Map.class,slot.get(0));
                List person = StringCommentUtils.get(List.class, map12.get("person"));
                Map<String,Object> mapNew = new HashMap<>();
                for (int j = 0; j < person.size(); j++) {
                    String sss = StringCommentUtils.rvZeroAndDot(person.get(j).toString());
                    person.set(j,sss);
                }
                mapNew.put("person",person);
                soltList.add(mapNew);
            }
            search_body.put("slot",soltList);

        }else  if(queryType.equals("危化品") && "B".equals(classType)){  //处理小数点问题
            List slot = StringCommentUtils.get(List.class, search_body.get("slot"));
            List<Map<String,Object>> soltListNew  = new ArrayList();
            for (int i = 0; i < slot.size(); i++) {
                Map  map12  = StringCommentUtils.get(Map.class,slot.get(i));
                List person = StringCommentUtils.get(List.class, map12.get("hazardous_chemical_material_name"));
                Map<String,Object> mapNew = new HashMap<>();
                for (int j = 0; j < person.size(); j++) {
                    String sss = StringCommentUtils.rvZeroAndDot(person.get(j).toString());
                    person.set(j,sss);
                    mapNew.put("hazardous_chemical_material_name",person);
                }
                soltListNew.add(mapNew);
            }
            search_body.put("slot",soltListNew);
        }

        try {
            url = gQueryBaseUrl + "/graph_search/query_result?type="+ queryType;
            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, gson.toJson(search_body));
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }


        if("队伍".equals(queryType)  || "专家".equals(queryType)){
            Map map1 = gson.fromJson(jsonStr, Map.class);
            Map<String,Object> map2 = StringCommentUtils.get(Map.class, map1.get("res"));
            List listRes = StringCommentUtils.get(List.class, map2.get("res"));
            Map<String, Object> stringObjectMap = PageHelp.newPagesMap(listRes, pageIndex, pageSize);
//            map2.put("page_info",stringObjectMap);
            map2.put("total_num",stringObjectMap.get("total_count"));
            map2.put("content",stringObjectMap.get("content"));
            map2.remove("res");
            Map map3 = new HashMap();
            map3.put("search_res",map2);
            map1.put("res",map3);
            jsonStr = gson.toJson(map1);
        }else if("装备".equals(queryType)){
            Map map1 = gson.fromJson(jsonStr, Map.class);
            Map<String,Object> map2 = StringCommentUtils.get(Map.class, map1.get("res"));
            Map map3 = new HashMap();
            map3.put("total_num",1);
            map3.put("content",map2);
            Map map4 = new HashMap();
            map4.put("search_res",map3);
            map1.put("res",map4);
            jsonStr = gson.toJson(map1);

        }else if("危化品".equals(queryType) && "B".equals(map.get("class").toString())){
            Map map1 = gson.fromJson(jsonStr, Map.class);
            Map<String,Object> map2 = StringCommentUtils.get(Map.class, map1.get("res"));
            Map map3 = new HashMap();
            map3.put("total_num",1);
            map3.put("content",map2);
            Map map4 = new HashMap();
            map4.put("search_res",map3);
            map1.put("res",map4);
            jsonStr = gson.toJson(map1);
        }else if("危化品".equals(queryType)  && "A".equals(map.get("class").toString()) ){
            Map map1 = gson.fromJson(jsonStr, Map.class);
            Map<String,Object> map2 = StringCommentUtils.get(Map.class, map1.get("res"));
            Map<String,Object> map3 = StringCommentUtils.get(Map.class, map2.get("hazardous_chemical_material_list"));
            List<Map<String,Object>>  hazardousList = StringCommentUtils.get(List.class, map3.get("res"));
            List<Map<String,Object>> knowledgeList = StringCommentUtils.get(List.class, map2.get("knowledge_card_list"));
            List<Map<String,Object>>  list = new ArrayList<>();

            Map<String, Object> hazMap = PageHelp.newPagesMap(hazardousList, pageIndex, pageSize);
            Map<String, Object> knowMap = PageHelp.newPagesMap(knowledgeList, pageIndex, pageSize);
            // map2.put("page_info",stringObjectMap);
            map2.put("total_num",hazMap.get("total_count"));


            map2.remove("res");
            map2.remove("hazardous_chemical_material_list");
            map2.remove("knowledge_card_list");
            Map map5 = new HashMap();
            map5.put("hazardous_chemical_material_list",hazMap.get("content"));
            map5.put("knowledge_card_list",knowMap.get("content"));
            map2.put("content",map5);

            Map map4 = new HashMap();
            map4.put("search_res",map2);
            map1.put("res",map4);
            jsonStr = gson.toJson(map1);
        }
        return jsonStr;
    }

    /**
     * 推荐类数据结果  只有分页信息以及请求槽
     * */
    @RequestMapping(value = "/v2/recommend_result", method = RequestMethod.POST)
    public String recommendResult(@RequestBody Map<String,Object> map) {
        int pageIndex = 1;
        int pageSize = 10;
        if(!"".equals(map.get("page_index").toString())){
            pageIndex = Integer.parseInt(map.get("page_index").toString());
            pageSize = Integer.parseInt(map.get("page_size").toString());
        }
        map.remove("page_index");
        map.remove("page_size");
        //拿到3的Body体
        map = StringCommentUtils.get(Map.class, map.get("optimal_res"));
        String queryType = map.get("type").toString();
        if("expert".equals(queryType)){
            queryType = "专家";
        }else if("rescue_team".equals(queryType)){
            queryType = "队伍";
        }else if("equipment".equals(queryType)){
            queryType = "装备";
        }else if("hazardous_chemical".equals(queryType)){
            queryType = "危化品";
        }
        String  classType123 = map.get("class").toString();// 前端展示分类
        if(queryType.equals("专家") && "B".equals(classType123)){  //处理小数点问题
            List slot = StringCommentUtils.get(List.class, map.get("slot"));
            List<Map<String,Object>> soltList  = new ArrayList();
            for (int i = 0; i < slot.size(); i++) {
                Map  map12  = StringCommentUtils.get(Map.class,slot.get(0));
                List person = StringCommentUtils.get(List.class, map12.get("person"));
                Map<String,Object> mapNew = new HashMap<>();
                for (int j = 0; j < person.size(); j++) {
                    String sss = StringCommentUtils.rvZeroAndDot(person.get(j).toString());
                    person.set(j,sss);
                }
                mapNew.put("person",person);
                soltList.add(mapNew);
            }
            map.put("slot",soltList);
        }else  if(queryType.equals("危化品") && "B".equals(classType123)){  //处理小数点问题
            List slot = StringCommentUtils.get(List.class, map.get("slot"));
            List<Map<String,Object>> soltListNew  = new ArrayList();
            for (int i = 0; i < slot.size(); i++) {
                Map  map12  = StringCommentUtils.get(Map.class,slot.get(i));
                List person = StringCommentUtils.get(List.class, map12.get("hazardous_chemical_material_name"));
                Map<String,Object> mapNew = new HashMap<>();
                for (int j = 0; j < person.size(); j++) {
                    String sss = StringCommentUtils.rvZeroAndDot(person.get(j).toString());
                    person.set(j,sss);
                    mapNew.put("hazardous_chemical_material_name",person);
                }
                soltListNew.add(mapNew);
            }
            map.put("slot",soltListNew);
        }
        System.out.println(gson.toJson(map));

        String url = null;
        String jsonStr = null;
        try {
            url = gSearchBaseUrl + "/search/v2/query_request_builder";
            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, gson.toJson(map));
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map resultMap = gson.fromJson(jsonStr, Map.class);
        Map<String,Object> reqMap = StringCommentUtils.get(Map.class, resultMap.get("res"));
        Boolean recommendFlag = StringCommentUtils.get(Boolean.class, reqMap.get("recommend_flag"));
        if(recommendFlag){  //true   存在
            Map recommendMap = StringCommentUtils.get(Map.class, reqMap.get("recommend_search_body"));

            String  classType = recommendMap.get("class").toString();// 前端展示分类
            if(queryType.equals("专家") && "B".equals(classType)){  //处理小数点问题
                List slot = StringCommentUtils.get(List.class, recommendMap.get("filter_slot"));
                List<Map<String,Object>> soltList  = new ArrayList();
                for (int i = 0; i < slot.size(); i++) {
                    Map  map12  = StringCommentUtils.get(Map.class,slot.get(0));
                    List person = StringCommentUtils.get(List.class, map12.get("person"));
                    Map<String,Object> mapNew = new HashMap<>();
                    for (int j = 0; j < person.size(); j++) {
                        String sss = StringCommentUtils.rvZeroAndDot(person.get(j).toString());
                        person.set(j,sss);
                    }
                    mapNew.put("person",person);
                    soltList.add(mapNew);
                }
                recommendMap.put("slot",soltList);
            }else  if(queryType.equals("危化品") && "B".equals(classType)){  //处理小数点问题
                List slot = StringCommentUtils.get(List.class, recommendMap.get("filter_slot"));
                List<Map<String,Object>> soltListNew  = new ArrayList();
                for (int i = 0; i < slot.size(); i++) {
                    Map  map12  = StringCommentUtils.get(Map.class,slot.get(i));
                    List person = StringCommentUtils.get(List.class, map12.get("hazardous_chemical_material_name"));
                    Map<String,Object> mapNew = new HashMap<>();
                    for (int j = 0; j < person.size(); j++) {
                        String sss = StringCommentUtils.rvZeroAndDot(person.get(j).toString());
                        person.set(j,sss);
                        mapNew.put("hazardous_chemical_material_name",person);
                    }
                    soltListNew.add(mapNew);
                }
                recommendMap.put("slot",soltListNew);
            }
            CloseableHttpResponse response = null;
            try {
                url = gQueryBaseUrl + "/graph_search/query_recommend?type="+ queryType;
                HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, gson.toJson(recommendMap));
                response = this.httpClient.execute(httpPost);
                jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if("队伍".equals(queryType) || "专家".equals(queryType) ){ //不包含 A B  都加分页
                Map map1 = gson.fromJson(jsonStr, Map.class);
                Map<String,Object> map2 = StringCommentUtils.get(Map.class, map1.get("res"));
                List listRes = StringCommentUtils.get(List.class, map2.get("res"));
                Map<String, Object> stringObjectMap = PageHelp.newPagesMap(listRes, pageIndex, pageSize);
                map2.put("total_num",stringObjectMap.get("total_count"));
                map2.put("content",stringObjectMap.get("content"));
                map2.remove("res");
                Map map3 = new HashMap();
                map3.put("recommend_res",map2);
                map1.put("res",map3);
                jsonStr = gson.toJson(map1);
            }else if("装备".equals(queryType)){
                Map map1 = gson.fromJson(jsonStr, Map.class);
                Map<String,Object> map2 = StringCommentUtils.get(Map.class, map1.get("res"));
                Map map3 = new HashMap();
                map3.put("total_num",1);
                map3.put("content",map2);
                Map map4 = new HashMap();
                map4.put("recommend_res",map3);
                map1.put("res",map4);
                jsonStr = gson.toJson(map1);

            }else if("危化品".equals(queryType)  && "B".equals(map.get("class").toString()) ){
                Map map1 = gson.fromJson(jsonStr, Map.class);
                List res = StringCommentUtils.get(List.class, map1.get("res"));
                Map map3 = new HashMap();
                map3.put("total_num",1);
                map3.put("content",res);
                Map map4 = new HashMap();
                map4.put("recommend_res",map3);
                map1.put("res",map4);
                jsonStr = gson.toJson(map1);
            }
            if (response.getStatusLine().getStatusCode() == 200) {
                return  jsonStr;
            }
        }else{
            return ResultMsg.successData();
        }
        return ResultMsg.successData();
    }


    /**
     * 通用类数据结果  只有分页信息以及请求槽
     * */
    @RequestMapping(value = "/v2/general_result", method = RequestMethod.POST)
    public String generalResult(@RequestBody Map<String,Object> map) {
        String jsonStr = "";

        map.put("page_size",Integer.parseInt(map.get("page_size").toString()));
        map.put("page_num",Integer.parseInt(map.get("page_num").toString()));
        try {
            //因为接口为直接覆盖接口，所以需要把已有用户先查询出来
            String url = gQueryBaseUrl + "/search/full_text_search";
            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, map);
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Map map1 = gson.fromJson(jsonStr, Map.class);
        Map<String,Object> map2 = StringCommentUtils.get(Map.class, map1.get("res"));
        List listRes = StringCommentUtils.get(List.class, map2.get("res"));
        map2.remove("res");
        map2.put("content",listRes);
        Map map3 = new HashMap();
        map3.put("general_res",map2);
        map1.put("res",map3);
        jsonStr = gson.toJson(map1);
        return jsonStr;

    }




    /**
     * 装备详情页点击
     * */
    @RequestMapping(value = "/v2/click_details", method = RequestMethod.POST)
    public String clickDetails(@RequestBody Map<String,Object> map) {
        String jsonStr = "";
        int pageIndex = 1;
        int pageSize = 10;
        if(!"".equals(map.get("page_index").toString())){
            pageIndex = Integer.parseInt(map.get("page_index").toString());
            pageSize = Integer.parseInt(map.get("page_size").toString());
        }

        map.remove("page_index");
        map.remove("page_size");
        try {
            //因为接口为直接覆盖接口，所以需要把已有用户先查询出来
            String url = gQueryBaseUrl + "/search/equipment/click_detail";
            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, map);
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map map1 = gson.fromJson(jsonStr, Map.class);
        Map<String,Object> map2 = StringCommentUtils.get(Map.class, map1.get("res"));
        map2 = StringCommentUtils.get(Map.class, map2.get("detail_res"));
        List listRes = StringCommentUtils.get(List.class, map2.get("res"));
        Map<String, Object> stringObjectMap = PageHelp.newPagesMap(listRes, pageIndex, pageSize);
        map2.put("total_num",stringObjectMap.get("total_count"));
        map2.put("content",stringObjectMap.get("content"));
        map2.remove("res");
        Map map3 = new HashMap();
        map3.put("detail_res",map2);
        map1.put("res",map3);
        jsonStr = gson.toJson(map1);
        return jsonStr;
    }



    /**
     * 通用点击详情
     * */
    @RequestMapping(value = "/v2/click_general", method = RequestMethod.POST)
    public String clickGeneralDetails(@RequestBody Map<String,Object> map) {
        String jsonStr = "";
        String  types = map.get("type").toString();

        map.remove("type");
        try {
            //因为接口为直接覆盖接口，所以需要把已有用户先查询出来
            String url = gQueryBaseUrl + "/search/show_detail?type="+ types ;
            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, map);
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonStr;
    }




//    @RequestMapping(value = "/v2/test_test_all", method = RequestMethod.POST)
//    public List testeststs(@RequestBody Map<String,Object> map) {
//        String sql = "MATCH (n:CorporateChemical) RETURN n LIMIT 25";
//        Neo4jTest1 Neo4jTest1 = new Neo4jTest1();
//        List  list  = new ArrayList();
//        try {
//            list =  StringCommentUtils.getNeo4jData(sql,Neo4jTest1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return  list;
//    }




}
