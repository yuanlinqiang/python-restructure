package com.gsafety.pythonrestructure.python.model;

import java.util.List;
import java.util.Map;


public class Hazardous {


    /**
     * record_id : 35201
     * res : [{"key":"名称","val":"硝酸胍"},{"key":"特别警示","val":"加热至150℃ 时分解并爆炸。"},{"key":"个体防护","val":null},{"key":"燃烧与爆炸危险性","val":"受热、接触明火、或受到摩擦、震动、撞击时可发生爆炸。加热至150℃ 时分解并爆炸。"}]
     * title : 硝酸胍
     */

    private String record_id;
    private String title;
    private List<ResBean> res;

    private Map<String,Object> knowledge;

    public Map<String, Object> getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(Map<String, Object> knowledge) {
        this.knowledge = knowledge;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ResBean> getRes() {
        return res;
    }

    public void setRes(List<ResBean> res) {
        this.res = res;
    }

    public static class ResBean {
        /**
         * key : 名称
         * val : 硝酸胍
         */

        private String key;
        private String val;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }
}
