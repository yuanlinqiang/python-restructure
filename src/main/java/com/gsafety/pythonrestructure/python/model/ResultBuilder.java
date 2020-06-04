package com.gsafety.pythonrestructure.python.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultBuilder {


    /**
     * code : 200
     * msg : success
     * res : {"class":"A","intent":[15],"query":"长春市人数大于30人的危化队伍","slot":[{"city":["长春市","attribute"],"team_size":["30,>","attribute"],"team_type":[776493,"class"]}],"type":"rescue_team"}
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
         * intent : [15]
         * query : 长春市人数大于30人的危化队伍
         * slot : [{"city":["长春市","attribute"],"team_size":["30,>","attribute"],"team_type":[776493,"class"]}]
         * type : rescue_team
         */

        @SerializedName("class")
        private String classX;
        private String query;
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
            private List<String> city;
            private List<String> team_size;
            private List<Integer> team_type;

            public List<String> getCity() {
                return city;
            }

            public void setCity(List<String> city) {
                this.city = city;
            }

            public List<String> getTeam_size() {
                return team_size;
            }

            public void setTeam_size(List<String> team_size) {
                this.team_size = team_size;
            }

            public List<Integer> getTeam_type() {
                return team_type;
            }

            public void setTeam_type(List<Integer> team_type) {
                this.team_type = team_type;
            }
        }
    }
}
