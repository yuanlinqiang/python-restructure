package com.gsafety.pythonrestructure.python.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultFrom {


    /**
     * code : 200
     * msg : success
     * res : {"class":"A","intent":[24],"query":"北京举高高度超过40米的消防车","slot":[{"left_height":["40,>","attribute"],"province":["北京市","attribute"]}],"type":"equipment"}
     * ret : true
     */

    private int code;
    private String msg;
    private ResBean res;
    private boolean ret;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResBean getRes() {
        return res;
    }

    public void setRes(ResBean res) {
        this.res = res;
    }

    public boolean isRet() {
        return ret;
    }

    public void setRet(boolean ret) {
        this.ret = ret;
    }

    public static class ResBean {
        /**
         * class : A
         * intent : [24]
         * query : 北京举高高度超过40米的消防车
         * slot : [{"left_height":["40,>","attribute"],"province":["北京市","attribute"]}]
         * type : equipment
         */

        @SerializedName("class")
        private String classX;
        private String query;
        private String query_data;
        private String type;
        private List<Integer> intent;
        private List<SlotBean> slot;

        public String getClassX() {
            return classX;
        }

        public void setClassX(String classX) {
            this.classX = classX;
        }

        public String getQuery() {
            return query;
        }

        public String getQuery_data() {
            return query_data;
        }

        public void setQuery_data(String query_data) {
            this.query_data = query_data;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Integer> getIntent() {
            return intent;
        }

        public void setIntent(List<Integer> intent) {
            this.intent = intent;
        }

        public List<SlotBean> getSlot() {
            return slot;
        }

        public void setSlot(List<SlotBean> slot) {
            this.slot = slot;
        }

        public static class SlotBean {
            private List<String> left_height;
            private List<String> province;

            public List<String> getLeft_height() {
                return left_height;
            }

            public void setLeft_height(List<String> left_height) {
                this.left_height = left_height;
            }

            public List<String> getProvince() {
                return province;
            }

            public void setProvince(List<String> province) {
                this.province = province;
            }
        }
    }
}
