package com.gsafety.pythonrestructure.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gsafety.pythonrestructure.common.Gson.NullStringToEmptyAdapterFactory;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ResultMsg<T> {

    public static final Gson gson = new Gson();
    public static final String SUCCESSFUL_CODE = "200";
    public static final String FAIL_CODE = "500";

    public static final String SUCCESSFUL_MESG = "操作成功";
    public static final String EXCEL_FAIL_MESG = "导入失败";
    public static final String EXCEL_SUCCESSFUL_MESG = "导入成功";
    public static final String LOGIN_SUCCESSFUL_MESG = "登录成功";
    public static final String FAIL_MESG = "操作失败，服务器异常";
    public static final String EXIT_PHONE = "改手机号已存在，请重新创建";

    private String code;
    private String mesg;

    private T data;

    public ResultMsg() {

    }

    /**
     * @param errorType
     */
    public ResultMsg(ErrorType errorType) {
        this.code = errorType.getCode();
        this.mesg = errorType.getMesg();
    }

    /**
     * @param errorType
     * @param data
     */
    public ResultMsg(ErrorType errorType, T data) {
        this(errorType);
        this.data = data;
    }

    /**
     * 内部使用，用于构造成功的结果
     * @param code
     * @param mesg
     * @param data
     */
    private ResultMsg(String code, String mesg, T data) {
        this.code = code;
        this.mesg = mesg;
        this.data = data;
    }



    /**
     * 传入JSON字符串，成功结果并返回结果数据
     * @param jsonData
     * @return Result
     */
    public static String  success(String jsonData) {
        String  resultJson = gson.toJson( new ResultMsg(SUCCESSFUL_CODE, SUCCESSFUL_MESG, gson.fromJson(jsonData,Map.class)));
        return resultJson;
    }


    public static String  success(List data) {
        String  resultJson = gson.toJson( new ResultMsg(SUCCESSFUL_CODE, SUCCESSFUL_MESG, data));
        return resultJson;
    }

    /**
     * 传入map，成功结果并返回结果数据
     * @param map
     * @return Result
     */
    public static String  success(Map map) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        String  resultJson = gson.toJson( new ResultMsg(SUCCESSFUL_CODE, SUCCESSFUL_MESG, map));
        return resultJson;
    }


    /**
     * 成功并直接返回
     * @return Result
     */
    public static String  exitPhone() {
        String  resultJson = gson.toJson( new ResultMsg(FAIL_CODE, EXIT_PHONE, null));
        return resultJson;
    }

    /**
     * 成功并直接返回
     * @return Result
     */
    public static String  success() {
        String  resultJson = gson.toJson( new ResultMsg(SUCCESSFUL_CODE, SUCCESSFUL_MESG, null));
        return resultJson;
    }

     /**
         * 操作失败并直接返回结果数据
         * @param msg
         * @return Result
         */
        public static String fails(String msg) {
            Map maps = gson.fromJson(msg, Map.class);
            String  code = maps.get("error").toString();
            //根据CODE码获取相关的异常信息
            SystemErrorType errorType = SystemErrorType.getByCode(code);
            if(errorType != null) return gson.toJson( new ResultMsg(errorType.getCode(), errorType.getMesg(), null));
            return gson.toJson( new ResultMsg(FAIL_CODE, FAIL_MESG, null));
        }


    /**
     * 操作失败并直接返回结果数据
     * @return Result
     */
    public static String fails() {
        String  resultJson = gson.toJson( new ResultMsg(FAIL_CODE, FAIL_MESG, null));
        return resultJson;
    }

    /**
     * 手机号存在信息
     * @return Result
     */
    public static String phoneFails(String Msg) {
        String  resultJson = gson.toJson( new ResultMsg(FAIL_CODE, EXCEL_FAIL_MESG, Msg));
        return resultJson;
    }

    public static String phoneSuccess(String Msg) {
        String  resultJson = gson.toJson( new ResultMsg(SUCCESSFUL_CODE, EXCEL_SUCCESSFUL_MESG, Msg));
        return resultJson;
    }

    /**
     * 登录成功
     * @return Result
     */
    public static String  loginInfoSuccess(String data) {
        String  resultJson = gson.toJson( new ResultMsg(SUCCESSFUL_CODE, LOGIN_SUCCESSFUL_MESG, gson.fromJson(data,Map.class)));
        return resultJson;
    }


    /**
     * 操作成功 但是数据结构为空
     * @return Result
     */
    public static String successData() {
        Map<String,Object> map = new HashMap<>();

        map.put("code",200);
        map.put("msg","Succeed");
        List<String> list  = new ArrayList<>();
        map.put("res",list);
        String  resultJson = gson.toJson(map);
        return resultJson;

    }



    public static String customizeSuccess(Map   map) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        Map<String,Object>   newMap = new HashMap<>();
        newMap.put("code",200);
        newMap.put("msg","Succeed");
        newMap.put("data",map);
        String  resultJson = gson.toJson(  newMap);
        return resultJson;
    }

    public static String customizeSuccessRes(Map   map) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        Map<String,Object>   newMap = new HashMap<>();
        newMap.put("code",200);
        newMap.put("msg","Succeed");
        newMap.put("res",map);
        String  resultJson = gson.toJson(  newMap);
        return resultJson;
    }


}
