package com.gsafety.pythonrestructure.Utils;

import com.google.gson.Gson;
import org.apache.http.entity.StringEntity;

public class StringCommentUtils {


    /**
     * 根据json字符串转换成原始类
     */
    public static  <T> T get(Class<T> clz,Object o){
        if(clz.isInstance(o)){
            return clz.cast(o);
        }
        return null;
    }

    /**
     * 转换JSON字符串
     */
    public static StringEntity getJsonStr(Object params){
        Gson gson = new Gson();
        String jsonString = gson.toJson(params);
        StringEntity jsonStr = new StringEntity(jsonString, "utf-8");
        jsonStr.setContentType("application/json");
        return jsonStr;
    }

    public static String removeTrim(String str){
        if(str.indexOf(".") > 0){
            str = str.replaceAll("0+?$", "");//去掉多余的0
            str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return str;
    }


    //处理小数点后面的位数
    public static String rvZeroAndDot(String s){
        if (s.isEmpty()) {
            return null;
        }
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }



}
