package com.gsafety.pythonrestructure.python.controller;

import com.google.gson.Gson;
import com.gsafety.pythonrestructure.Utils.PageHelp;
import com.gsafety.pythonrestructure.Utils.StringCommentUtils;
import com.gsafety.pythonrestructure.common.HttpClientBuildConfig;
import com.gsafety.pythonrestructure.common.ResultMsg;
import com.gsafety.pythonrestructure.config.oauth2.neo4j.Neo4jConfig;
import com.gsafety.pythonrestructure.python.model.EquipmentEnmu;
import com.gsafety.pythonrestructure.python.model.ResultFromAll;
import org.apache.http.ParseException;
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
@RequestMapping("/python/reconstruction/api")
public class ReconstructionController {


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

//        Thread threadOne = null;
//
//        try {
//            threadOne = new Thread(new Runnable() {
//                public void run() {
//                    searchIntent(map);
//                }
//            });
//            Thread threadTwo = new Thread(new Runnable() {
//                public void run() {
//                    searchMentionLink(map);
//                }
//            });
//            // 执行线程
//            threadOne.start();
//            threadTwo.start();
//            Thread.sleep(2000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        String searchIntentResult = searchIntent(map);
        String  searchMentionLink = searchMentionLink(map);


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
    public String searchIntent(@RequestBody Map<String,Object>  map)  {
        try {
            //因为接口为直接覆盖接口，所以需要把已有用户先查询出来
            String url = gSearchBaseUrl + "/search/v2/search_query_intent_cls";
            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, map);
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (response.getStatusLine().getStatusCode() == 200) {
                searchIntentResult = jsonStr;
                return jsonStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return ResultMsg.successData();
    }


    /**
     * 职称要素识别
     * */
    @RequestMapping(value = "/v2/search_query_mention_link", method = RequestMethod.POST)
    public String searchMentionLink(@RequestBody Map<String,Object>  map) {
        try {
            //因为接口为直接覆盖接口，所以需要把已有用户先查询出来
            String url = gSearchBaseUrl + "/search/v2/search_query_mention_link";
            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, map);
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (response.getStatusLine().getStatusCode() == 200) {
                searchMentionLink = jsonStr;
                return  jsonStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultMsg.successData();
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
     * 根据三的请求结果处理构建body体    出现危化品名称 也会推荐
     *      危化品   search边     re  不变
     * */
    @RequestMapping(value = "/v2/query_builder_body", method = RequestMethod.POST)
    public String  queryBuilderBody(@RequestBody Map<String,Object> map) {

        String queryType = map.get("type").toString();
        Map recommendFlagMap = new HashMap();
        List<Map<String,Object>> slot = StringCommentUtils.get(List.class, map.get("slot")); //省市区数据集合
        List<Map<String,Object>> copySlot = StringCommentUtils.get(List.class, map.get("slot")); //省市区数据集合

        Boolean   flag = false;
        Boolean  districtFlag = false;
        Boolean  cityFlag = false;
        String districtName ="";
        String cityName ="";

        for (int i = 0; i < slot.size(); i++) {
            Map<String, Object> stringObjectMap = slot.get(i);
            for (String key: stringObjectMap.keySet()){
                if(key.equals("district")){
                    flag  = true;
                    List district = StringCommentUtils.get(List.class, stringObjectMap.get("district"));
                    districtName = district.get(0).toString();//取第一个值作为参数
                    districtFlag = true;
                    continue;
                }else if(key.equals("city")){
                    cityFlag = true;
                    flag  = true;
                    List city = StringCommentUtils.get(List.class, stringObjectMap.get("city"));
                    cityName = city.get(0).toString();//取第一个值作为参数
                    continue;
                }
            }
        }

        //处理省份
        String districtOrCityCypher = "";
        List<Map<String, Object>> list = new ArrayList();
        String father_name = "";
        List<String> provinceList = new ArrayList<>();


        //处理solt
        List<Map<String,Object>> recommandList = new ArrayList<>(); //推荐数据集合
        Boolean  hazardous = false;
        List<Map<String,Object>> searchList = new ArrayList<>();
        List  newRecommendList = new ArrayList();
        List<Map<String,Object>>  recList= new ArrayList<>();  //封装集合的数据


        for (int i = 0; i < copySlot.size(); i++) {
            Map<String, Object> stringObjectMap = copySlot.get(i);
            Map<String,Object> newFilterMap = new HashMap<>();
            Map<String,Object> newRecommendMap = new HashMap<>();
            for (String key:stringObjectMap.keySet()){
                if(key.equals("hazardous_chemical_material_name")  && queryType.equals("expert") ){ // 专家类含有危化品
                    List<Object> hazList2 = StringCommentUtils.get(List.class,stringObjectMap.get(key));
                    String s1 = StringCommentUtils.rvZeroAndDot(hazList2.get(0).toString());
                    int  id = Integer.parseInt(s1);
                    String hazCypher = "match (n:HazardousChemicalsMaterial) where id(n)= "+id+" return n.name as name";
                    List<Map<String,Object>> hazList = executeSql(hazCypher);
                    newRecommendList.add(hazList.get(0).get("name").toString());
                    newRecommendList.add("attribute");
                    newFilterMap.put("hazardous_chemical_material_name",newRecommendList);
                    newRecommendMap.put("hazardous_chemical_material_name",newRecommendList);
                    hazardous = true;
                    flag = true;
                    continue;
                }if(key.equals("hazardous_chemical_material_name")  && map.get("class").equals("B") ){ // 专家类含有危化品
                    List<Object> hazList2 = StringCommentUtils.get(List.class,stringObjectMap.get(key));
                    String s1 = StringCommentUtils.rvZeroAndDot(hazList2.get(0).toString());
                    int  id = Integer.parseInt(s1);
                    String hazCypher = "match (n:HazardousChemicalsMaterial) where id(n)= "+id+" return n.name as name";
                    List<Map<String,Object>> hazList = executeSql(hazCypher);
                    newRecommendList.add(hazList.get(0).get("name").toString());
                    newRecommendList.add("attribute");
                    newFilterMap.put("hazardous_chemical_material_name",newRecommendList);
                    newRecommendMap.put("hazardous_chemical_material_name",newRecommendList);
                    hazardous = true;
                    flag = true;
                    continue;
                }else if(key.equals("left_height")){ //装备
                    searchList = StringCommentUtils.get(List.class,stringObjectMap.get(key));
                    String[] params = StringCommentUtils.get(String.class, searchList.get(0)).split(",");
                    newRecommendList.add(Integer.parseInt(params[0]));
                    newRecommendList.add("attribute");
                    newRecommendList.add(params[1]);
                    newFilterMap.put("left_height",newRecommendList);
                    newRecommendMap.put("left_height",newRecommendList);
                    continue;
                }else if(key.equals("team_size")){ //队伍
                    searchList = StringCommentUtils.get(List.class,stringObjectMap.get(key));
                    String[] params = StringCommentUtils.get(String.class, searchList.get(0)).split(",");
                    newRecommendList.add(Integer.parseInt(params[0].toString()));
                    newRecommendList.add("attribute");
                    newRecommendList.add(params[1]);
                    newFilterMap.put("team_size",newRecommendList);
                    newRecommendMap.put("team_size",newRecommendList);
                    continue;
                }else if(key.equals("district")){
                    districtOrCityCypher = "match (n:Reginalism)-[:belong]->(m:Reginalism)-[:belong]->(p:Reginalism) where n.name='" + districtName + "' return n.name as child_name, p.name as father_name";
                    list = executeSql(districtOrCityCypher);
                    father_name = list.get(0).get("father_name").toString();
                    provinceList.add(father_name);
                    provinceList.add("attribute");
                    newRecommendMap.put("province",provinceList);
                    newFilterMap.put(key,stringObjectMap.get(key));
                    continue;
                }else if(key.equals("city")){
                    districtOrCityCypher = "match (n:Reginalism)-[:belong]->(m:Reginalism) where n.name='" + cityName + "' return n.name as child_name, m.name as father_name";
                    list = executeSql(districtOrCityCypher);
                    father_name = list.get(0).get("father_name").toString();
                    provinceList.add(father_name);
                    provinceList.add("attribute");
                    newFilterMap.put(key,stringObjectMap.get(key));
                    newRecommendMap.put("province",provinceList);
                    continue;
                }
                newFilterMap.put(key,stringObjectMap.get(key));
                newRecommendMap.put(key,stringObjectMap.get(key));

            }
            recList.add(newFilterMap);
            recommandList.add(newRecommendMap);
            if(hazardous) break;
        }


        //判断是否true   false   如果是转件也是true
        recommendFlagMap.put("recommend_flag",flag);

        //2  推荐
        Map recommendSearchBody = new HashMap();
        recommendSearchBody.put("class",map.get("class"));
        recommendSearchBody.put("query_data",map.get("query"));
        recommendSearchBody.put("template",map.get("intent"));
        recommendSearchBody.put("filter_slot",recList);
        Map<String,Object> mapA = new HashMap<>();
        recommendSearchBody.put("recommend_slot",recommandList);

        //3   构建search_body
        Map<String,Object> searchBodyMap = new HashMap<>();
        searchBodyMap.put("class",map.get("class"));
        searchBodyMap.put("query_data",map.get("query"));
        searchBodyMap.put("template",map.get("intent"));
        recommendFlagMap.put("recommend_search_body",recommendSearchBody);

        //2  构建recommend_search_body
        if(map.get("type").equals("hazardous_chemical") ){ //危化品
            searchBodyMap.put("slot",copySlot);
        }else { //专家、队伍、装备
            searchBodyMap.put("slot",recList);
        }
        recommendFlagMap.put("search_body",searchBodyMap);
        return  ResultMsg.customizeSuccessRes(recommendFlagMap);
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



    //==================================================魏宁封装数据接口======================================


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


        //数据封装 全局
        Map<String,Object> mapSearchRes = new HashMap<>();

        //空的话不走搜索和推荐
        Map res = StringCommentUtils.get(Map.class, reqMap.get("res"));
        if(reqMap.get("type") != null){
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


            mapSearchRes.put("search_res",gson.fromJson(gson.toJson(searchResultMap.get("search_res")), Map.class));  //查询类结果
            List   list = new ArrayList();
            mapSearchRes.put("recommend_res", recommendResultMap ==null ?list : gson.fromJson(gson.toJson(recommendResultMap.get("recommend_res")), Map.class));  //查询类结果

        }



        //通用
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
        if (!"".equals(map.get("page_index").toString())) {
            pageIndex = Integer.parseInt(map.get("page_index").toString());
            pageSize = Integer.parseInt(map.get("page_size").toString());
        }
        map.remove("page_index");
        map.remove("page_size");
        //拿到3的Body体
        map = StringCommentUtils.get(Map.class, map.get("optimal_res"));
        String queryType = map.get("type").toString();
        if ("expert".equals(queryType)) {
            queryType = "专家";
        } else if ("rescue_team".equals(queryType)) {
            queryType = "队伍";
        } else if ("equipment".equals(queryType)) {
            queryType = "装备";
        } else if ("hazardous_chemical".equals(queryType)) {
            queryType = "危化品";
        }
        //处理小数点后面有0的情况
        map = getQueryMap(map,queryType);
        String url = null;
//        String jsonStr = "";
//        try {
//            url = gSearchBaseUrl + "/search/v2/query_request_builder";
//            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, gson.toJson(map));
//            CloseableHttpResponse response = this.httpClient.execute(httpPost);
//            jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        String jsonStr = queryBuilderBody(map);

        Map resultMap = gson.fromJson(jsonStr, Map.class);
        Map<String, Object> reqMap = StringCommentUtils.get(Map.class, resultMap.get("res"));
        Map search_body = StringCommentUtils.get(Map.class, reqMap.get("search_body"));
        search_body = getBuildMap(search_body, queryType);
        String  classType = search_body.get("class").toString();// 前端展示分类

        Map returnMap = new HashMap();
        returnMap = search_body;

        if (!"危化品".equals(queryType)) {
            try {
                url = gQueryBaseUrl + "/graph_search/query_template?type=" + queryType;
                HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, gson.toJson(search_body));
                CloseableHttpResponse response = this.httpClient.execute(httpPost);
                jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

            Map resMap = gson.fromJson(jsonStr, Map.class);
            List list = StringCommentUtils.get(List.class, resMap.get("res"));

            List<Map<String, Object>> resList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                resList = executeSql(list.get(i).toString());  // 所有的数据集合
            }
            Map teamMap = new HashMap();
            if ("队伍".equals(queryType)) {
                Map<String, Object> stringObjectMap = teamSearchRecommand(resList, pageIndex, pageSize);
                teamMap.put("search_res", stringObjectMap);
            } else if ("专家".equals(queryType)) {
                Map<String, Object> stringObjectMap = expertSearchRecommand(resList, pageIndex, pageSize);
                teamMap.put("search_res", stringObjectMap);
            } else if ("装备".equals(queryType)) {
                Map<String, Object> stringObjectMap = equipmentSearch(resList, returnMap, pageIndex, pageSize);
                teamMap.put("search_res", stringObjectMap);
            }
            return ResultMsg.customizeSuccessRes(teamMap);
        }else{ //危化品直接调取相关的接口
            Map<String, Object> stringObjectMap = hazardousSearch(search_body, classType,queryType,pageIndex,pageSize);
            return ResultMsg.customizeSuccessRes(stringObjectMap);
        }
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
        map = getQueryMap(map,queryType);
        String url = null;
//        String jsonStr = null;
//        try {
//            url = gSearchBaseUrl + "/search/v2/query_request_builder";
//            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, gson.toJson(map));
//            CloseableHttpResponse response = this.httpClient.execute(httpPost);
//            jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        String jsonStr = queryBuilderBody(map);
        Map resultMap = gson.fromJson(jsonStr, Map.class);
        Map<String,Object> reqMap = StringCommentUtils.get(Map.class, resultMap.get("res"));
        Boolean recommendFlag = StringCommentUtils.get(Boolean.class, reqMap.get("recommend_flag"));
        if (recommendFlag) {  //true   存在
            Map recommendMap = getRecommandMap(reqMap, queryType);
            String  classType = recommendMap.get("class").toString();// 前端展示分类
            CloseableHttpResponse response = null;
            Map teamMap = new HashMap();
            if (!"危化品".equals(queryType)) {
                try {
                    url = gQueryBaseUrl + "/graph_search/recommend_template?type=" + queryType;
                    HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, gson.toJson(recommendMap));
                    response = this.httpClient.execute(httpPost);
                    jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Map resMap = gson.fromJson(jsonStr, Map.class);
                List list = StringCommentUtils.get(List.class, resMap.get("res"));
                List<Map<String, Object>> resList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    resList = executeSql(list.get(i).toString());  // 所有的数据集合
                }
                if ("队伍".equals(queryType)) { //不包含 A B  都加分页
                    Map<String, Object> stringObjectMap = teamSearchRecommand(resList, pageIndex, pageSize);
                    teamMap.put("recommend_res", stringObjectMap);
                } else if ("专家".equals(queryType)) { //不包含 A B  都加分页
                    Map<String, Object> stringObjectMap = expertSearchRecommand(resList, pageIndex, pageSize);
                    teamMap.put("recommend_res", stringObjectMap);
                } else if ("装备".equals(queryType)) { //不包含 A B  都加分页
                    Map<String, Object> stringObjectMap = equipmentRecommand(resList, recommendMap, pageIndex, pageSize);
                    teamMap.put("recommend_res", stringObjectMap);
                }
            }else {
                Map<String, Object> stringObjectMap = hazardousRecommend(recommendMap, classType, queryType);



                return ResultMsg.customizeSuccessRes(stringObjectMap);
            }
            if (response.getStatusLine().getStatusCode() == 200) {
                return ResultMsg.customizeSuccessRes(teamMap);
            }
        } else {
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
            String url = gQueryBaseUrl + "/search/equipment/click_template";

            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, map);
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Map resMap = gson.fromJson(jsonStr, Map.class);
        List list = StringCommentUtils.get(List.class, resMap.get("res"));

        List<Map<String, Object>> resList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            resList = executeSql(list.get(i).toString());  // 所有的数据集合
        }

        Map<String, Object> stringObjectMap = equimentClieck(resList, pageIndex, pageSize); //封装数据结构
        return ResultMsg.customizeSuccessRes(stringObjectMap);
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




    /**
     * 队伍查询推荐封装类
     * */
    public Map<String,Object> teamSearchRecommand(List<Map<String,Object>>  list,int pageIndex,int pageSize) {

        Map provinceMap = new HashMap();
        //拿到外层的数据信息
        list.stream().forEach(e ->{
            int count = 1;
            if(provinceMap.containsKey(e.get("province_code"))){
                count = Integer.parseInt(provinceMap.get(e.get("province_code")).toString());
                count+=1;
            }
            provinceMap.put(e.get("province_code"),count);
        });

        Map<String, Object> contentMap = PageHelp.newPagesMap(list, pageIndex, pageSize);
        List<Map<String,Object>> contentList = StringCommentUtils.get(List.class, contentMap.get("content"));
        List<Map<String,Object>>  resultList = new ArrayList<>();
        Map map = new HashMap();
        for (int i = 0; i < contentList.size(); i++) {
            Map<String, Object> stringObjectMap = contentList.get(i);
            Map<String,Object> newMap = new HashMap<>();
            newMap.put("equipment_type",stringObjectMap.get("team_equipment"));
            newMap.put("equipment_type_detail",stringObjectMap.get("team_equipment_detail"));
            newMap.put("latitude",stringObjectMap.get("latitude"));
            newMap.put("longitude",stringObjectMap.get("longitude"));
            newMap.put("province_code",stringObjectMap.get("province_code"));
            newMap.put("record_id",stringObjectMap.get("id"));

            Map resMap = new HashMap();
            List<Map<String,Object>> resList = new ArrayList<>();


            Map leaderMap = new HashMap();
            leaderMap.put("val", (stringObjectMap.get("leader")));
            leaderMap.put("key","联系人");
            resList.add(leaderMap);

            Map rescueTypeMap = new HashMap();
            rescueTypeMap.put("val", (stringObjectMap.get("rescue_type")));
            rescueTypeMap.put("key","类型");
            resList.add(rescueTypeMap);

            Map cityMap = new HashMap();
            cityMap.put("val", (stringObjectMap.get("province")==null?"":stringObjectMap.get("province").toString())+(stringObjectMap.get("city")==null?"":stringObjectMap.get("city").toString())+ (stringObjectMap.get("district")==null?"":stringObjectMap.get("district").toString()) );
            cityMap.put("key","行政区划");
            resList.add(cityMap);

            Map phoneMap = new HashMap();
            phoneMap.put("val",stringObjectMap.get("leader_tel") ==null?"":stringObjectMap.get("leader_tel"));
            phoneMap.put("key","电话");
            resList.add(phoneMap);

            Map totalMap = new HashMap();
            totalMap.put("key","总人数");
            totalMap.put("val",list.size());
            resList.add(totalMap);


            newMap.put("res",resList); //不确定的个数
            newMap.put("title",stringObjectMap.get("name"));
            resultList.add(newMap);

        }
        map.put("content",resultList);
        map.put("total_num",list.size());
        map.put("location_info",provinceMap);


        return  map;
    }

    /**
     * 专家查询推荐封装类
     * */
    public Map<String,Object> expertSearchRecommand(List<Map<String,Object>>  list,int pageIndex,int pageSize) {
        Map provinceMap = new HashMap();
        list.stream().forEach(e ->{
            int count = 1;
            if(provinceMap.containsKey(e.get("province_code"))){
                count = Integer.parseInt(provinceMap.get(e.get("province_code")).toString());
                count+=1;
            }
            provinceMap.put(e.get("province_code"),count);
        });
        Map<String, Object> contentMap = PageHelp.newPagesMap(list, pageIndex, pageSize);
        List<Map<String,Object>> contentList = StringCommentUtils.get(List.class, contentMap.get("content"));
        List<Map<String,Object>>  resultList = new ArrayList<>();
        Map map = new HashMap();
        for (int i = 0; i < contentList.size(); i++) {
            Map<String, Object> stringObjectMap = contentList.get(i);
            Map<String,Object> newMap = new HashMap<>();
            newMap.put("id",stringObjectMap.get("id"));

            List<Map<String,Object>> infoList = new ArrayList<>();
            Map expertTypeMap = new HashMap();
            expertTypeMap.put("val",stringObjectMap.get("expert_type"));
            expertTypeMap.put("key","专业类型");
            infoList.add(expertTypeMap);
            Map areaMap = new HashMap();
            areaMap.put("val",(stringObjectMap.get("province")==null?"":stringObjectMap.get("province").toString())+(stringObjectMap.get("city")==null?"":stringObjectMap.get("city").toString())+ (stringObjectMap.get("district")==null?"":stringObjectMap.get("district").toString())  );
            areaMap.put("key","行政区划");
            infoList.add(areaMap);
            Map unionMap = new HashMap();
            unionMap.put("val",stringObjectMap.get("dept_name"));
            unionMap.put("key","工作单位");
            infoList.add(unionMap);
            newMap.put("info",infoList); //不确定的个数

            newMap.put("labels",stringObjectMap.get("skill_labels"));
            newMap.put("latitude",stringObjectMap.get("latitude"));
            newMap.put("longitude",stringObjectMap.get("longitude"));
            newMap.put("province_code",stringObjectMap.get("province_code"));
            newMap.put("phone_number",stringObjectMap.get("phone_number"));
            newMap.put("title",stringObjectMap.get("name"));
            resultList.add(newMap);
        }
        map.put("content",resultList);
        map.put("total_num",list.size());
        map.put("location_info",provinceMap);
        return  map;
    }


    /**
     * 装备查询封装类
     * */
    public Map<String,Object> equipmentSearch(List<Map<String,Object>>  list,Map returnMap,int pageIndex,int pageSize) {

        Map<String, Object> contentMap = PageHelp.newPagesMap(list, pageIndex, pageSize);
        List<Map<String,Object>> contentList = StringCommentUtils.get(List.class, contentMap.get("content"));

        List<Map<String,Object>>  resultList = new ArrayList<>();
        Map map = new HashMap();
        int totalClassNum = 0;
        int totalObjectNum = 0;
        for (int i = 0; i < contentList.size(); i++) {

            Map<String, Object> stringObjectMap = contentList.get(i);
            Map<String,Object> newMap = new HashMap<>();

            List<Map<String,Object>> listRes = new ArrayList<>();
            List detailedEquipmentDetail = StringCommentUtils.get(List.class, stringObjectMap.get("detailed_equipment_detail"));

            for (int j = 0; j < detailedEquipmentDetail.size(); j++) {
                Map childrenMap = new HashMap();
                List dataList = StringCommentUtils.get(List.class, detailedEquipmentDetail.get(j));
                List list1 = StringCommentUtils.get(List.class, dataList.get(0));
                childrenMap.put("record_id",list1.get(0));
                childrenMap.put("key",list1.get(1));
                childrenMap.put("type",list1.get(2));
                childrenMap.put("val",dataList.get(1));
                listRes.add(childrenMap);
            }
            newMap.put("children_list",listRes);
            newMap.put("class_num",stringObjectMap.get("class_num"));
            totalClassNum += Integer.parseInt(stringObjectMap.get("class_num").toString());
            newMap.put("equipment_type",stringObjectMap.get("general_equipment"));
            newMap.put("object_num",stringObjectMap.get("object_num"));
            totalObjectNum += Integer.parseInt(stringObjectMap.get("object_num").toString());
            newMap.put("record_id",stringObjectMap.get("general_equipment_id"));
            resultList.add(newMap);
        }

        Map requimentMap = new HashMap();

        requimentMap.put("base_static_res",resultList);
        requimentMap.put("class",returnMap.get("class"));

        List slot = StringCommentUtils.get(List.class, returnMap.get("slot"));
        Map  soltMap = new HashMap();
        soltMap.put("slot_list",slot);
        requimentMap.put("constraint",soltMap);
        requimentMap.put("total_class_num",totalClassNum);
        requimentMap.put("total_object_num",totalObjectNum);

        map.put("content",requimentMap);
        map.put("total_num",list.size());

        return  map;
    }

    /**
     * 危化品查询封装类
     * */
    public Map<String,Object> hazardousSearch(Map search_body,String classType,String queryType,int pageIndex,int pageSize) {
        String jsonStr = "";
        try {
            String url = gQueryBaseUrl + "/graph_search/query_result?type="+ queryType;
            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, gson.toJson(search_body));
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map map1 = gson.fromJson(jsonStr, Map.class);
        Map map4 = new HashMap();
       if("A".equals(classType) ){
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
            map4.put("search_res",map2);
        }else if( "B".equals(classType)){
           Map<String,Object> map2 = StringCommentUtils.get(Map.class, map1.get("res"));
           Map map3 = new HashMap();
           map3.put("total_num",1);
           map3.put("content",map2);
           map4.put("search_res",map3);
       }
        return map4;
    }

    /**
     * 危化品推荐封装类
     * */
    public Map<String,Object> hazardousRecommend(Map recommendMap,String classType,String queryType) {
        String jsonStr = "";
        CloseableHttpResponse response = null;
        try {
            String url =  gQueryBaseUrl + "/graph_search/query_recommend?type="+ queryType;
            HttpPost httpPost = httpClientBuildConfig.getHttpPost(url, gson.toJson(recommendMap));
            response = this.httpClient.execute(httpPost);
            jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map map1 = gson.fromJson(jsonStr, Map.class);
        Map map4 = new HashMap();
        if("B".equals(classType) ){
            List res = StringCommentUtils.get(List.class, map1.get("res"));
            Map map3 = new HashMap();
            map3.put("total_num",1);
            map3.put("content",res);
            map4.put("recommend_res",map3);
        }
        return map4;
    }

    /**
     * 装备推荐封装类
     * */
    public Map<String,Object> equipmentRecommand(List<Map<String,Object>>  list,Map returnMap,int pageIndex,int pageSize) {

        Map<String, Object> contentMap = PageHelp.newPagesMap(list, pageIndex, pageSize);
        List<Map<String,Object>> contentList = StringCommentUtils.get(List.class, contentMap.get("content"));

        List<Map<String,Object>>  resultList = new ArrayList<>();
        Map map = new HashMap();
        int totalClassNum = 0;
        int totalObjectNum = 0;
        for (int i = 0; i < contentList.size(); i++) {

            Map<String, Object> stringObjectMap = contentList.get(i);
            Map<String,Object> newMap = new HashMap<>();

            List<Map<String,Object>> listRes = new ArrayList<>();
            List detailedEquipmentDetail = StringCommentUtils.get(List.class, stringObjectMap.get("detailed_equipment_detail"));

            for (int j = 0; j < detailedEquipmentDetail.size(); j++) {
                Map childrenMap = new HashMap();
                List dataList = StringCommentUtils.get(List.class, detailedEquipmentDetail.get(j));
                List list1 = StringCommentUtils.get(List.class, dataList.get(0));
                childrenMap.put("record_id",list1.get(0));
                childrenMap.put("key",list1.get(1));
                childrenMap.put("type",list1.get(2));
                childrenMap.put("val",dataList.get(1));
                listRes.add(childrenMap);
            }
            newMap.put("children_list",listRes);

            newMap.put("class_num",stringObjectMap.get("class_num"));
            totalClassNum += Integer.parseInt(stringObjectMap.get("class_num").toString());
            newMap.put("equipment_type",stringObjectMap.get("general_equipment"));
            newMap.put("object_num",stringObjectMap.get("object_num"));
            totalObjectNum += Integer.parseInt(stringObjectMap.get("object_num").toString());
            newMap.put("record_id",stringObjectMap.get("general_equipment_id"));
            resultList.add(newMap);
        }
        Map requimentMap = new HashMap();
        requimentMap.put("base_static_res",resultList);
        requimentMap.put("class",returnMap.get("class"));
        List slot = StringCommentUtils.get(List.class, returnMap.get("slot"));
        Map  soltMap = new HashMap();
        soltMap.put("filter_slot",returnMap.get("filter_slot"));
        soltMap.put("recommend_slot",returnMap.get("recommend_slot"));
        requimentMap.put("constraint",soltMap);

        requimentMap.put("total_class_num",totalClassNum);
        requimentMap.put("total_object_num",totalObjectNum);
        map.put("content",requimentMap);
        map.put("total_num",1);

        return  map;
    }

    @RequestMapping(value = "/v2/execut", method = RequestMethod.POST)
    public List executeSql(String  sql) {
        List  list  = new ArrayList();
        try {
            list =  Neo4jConfig.getNeo4jData(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  list;
    }

    /**
     * 装备点击详情页封装类
     * */
    public Map<String,Object> equimentClieck(List<Map<String,Object>>  list,int pageIndex,int pageSize) {

        Map<String, Object> contentMap = PageHelp.newPagesMap(list, pageIndex, pageSize);
        List<Map<String,Object>> contentList = StringCommentUtils.get(List.class, contentMap.get("content"));
        List<Map<String,Object>>  resultList = new ArrayList<>();
        Map map = new HashMap();
        int  totalNum  = 0;
        for (int i = 0; i < contentList.size(); i++) {
            Map<String, Object> stringObjectMap = contentList.get(i);
            List<Map<String,Object>> teamDetailList = StringCommentUtils.get(List.class, stringObjectMap.get("team_detail"));
            for (int j = 0; j < teamDetailList.size(); j++) {
                totalNum = teamDetailList.size();
                Map  teamDetailMap = teamDetailList.get(j);
                Map<String,Object> newMap = new HashMap<>();
                newMap.put("equipment_type_record_id",stringObjectMap.get("detailed_equipment_id"));

                List<Map<String,Object>> resList = new ArrayList<>();
                Map leaderMap = new HashMap();
                leaderMap.put("val", (teamDetailMap.get("team_name")));
                leaderMap.put("key","所属队伍");
                resList.add(leaderMap);
                Map cityMap = new HashMap();
                cityMap.put("val", (teamDetailMap.get("province")==null?"":teamDetailMap.get("province").toString())+(teamDetailMap.get("city")==null?"":teamDetailMap.get("city").toString())+ (teamDetailMap.get("district")==null?"":teamDetailMap.get("district").toString()) );
                cityMap.put("key","行政区划");
                resList.add(cityMap);
                Map phoneMap = new HashMap();
                phoneMap.put("val",teamDetailMap.get("leader_tel") ==null?"":teamDetailMap.get("leader_tel"));
                phoneMap.put("key","电话");
                resList.add(phoneMap);
                Map nameMap = new HashMap();
                nameMap.put("val",teamDetailMap.get("leader"));
                nameMap.put("key","联系人");
                resList.add(nameMap);
                newMap.put("info_list",resList); //不确定的个数

                newMap.put("latitude",teamDetailMap.get("latitude"));
                newMap.put("longitude",teamDetailMap.get("longitude"));

                List<Map<String,Object>> equipmentDetailList = StringCommentUtils.get(List.class,teamDetailMap.get("equipment_detail"));
                StringBuffer sb = new StringBuffer();

                List<Map<String,Object>> equipmentList = new ArrayList<>();

                for (int k = 0; k < equipmentDetailList.size(); k++) {
                    String resultStr = gson.toJson(equipmentDetailList.get(k));
                    Map<String,Object> equipmentMap = gson.fromJson(resultStr, Map.class);
                    for (String key:equipmentMap.keySet()){
                        String msgKey = null;
                        try {
                            msgKey = EquipmentEnmu.valueOf(key).getMsg();
                        } catch (IllegalArgumentException e) {
                           continue;
                        }
                        Map<String,Object> equipmentDetailMap = new HashMap<>();
                        String msgValue = equipmentMap.get(key).toString();
                        sb.append(msgKey).append(":").append(msgValue).append(";");
                        equipmentDetailMap.put("main_args_name",msgKey);
                        equipmentDetailMap.put("main_args_value",msgValue);
                        equipmentList.add(equipmentDetailMap);
                    }
                }
                newMap.put("main_args",sb.toString());
                newMap.put("main_args_detail",equipmentList);
                newMap.put("number",teamDetailMap.get("team_equip_num"));
                newMap.put("province_code",teamDetailMap.get("province_code"));
                newMap.put("team_record_id",teamDetailMap.get("team_id"));
                newMap.put("title",stringObjectMap.get("detailed_equipment"));
                resultList.add(newMap);
            }
        }
        map.put("total_num",totalNum);
        map.put("content",resultList);
        return  map;
    }

    public  Map getQueryMap(Map map,String queryType){
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
        return map;
    }

    public  Map getBuildMap(Map search_body,String queryType){
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
        return search_body;
    }

    public  Map getRecommandMap(Map reqMap,String queryType){

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
        return  recommendMap;
    }




}
