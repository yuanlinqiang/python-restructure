package com.gsafety.pythonrestructure.python.model;

import java.util.List;

public class ResultFromAll {


    /**
     * code : 200
     * msg : success
     * res : {"label_prob":0.9996978044509888,"label_type_index":49,"processed_query":"长春市人数大于30人的危化队伍","_mention_slot_list":[["expert_industry","skill","skill","team_type"],["team"]],"_number_slot_list":[["team_size"]],"_regionalism_slot_list":[["city"]],"_temp_slot_list":[["expert_industry","skill","skill","team_type"]],"candidate_mention_list":["危化","队伍"],"location_mention_list":["长春市"],"mention_slot_list":[[{"slot_name":"expert_industry","slot_type":"class","slot_value":782261},{"slot_name":"skill","slot_type":"class","slot_value":776103},{"slot_name":"skill","slot_type":"class","slot_value":776117},{"slot_name":"team_type","slot_type":"class","slot_value":776493}],[{"slot_name":"team","slot_type":"extra","slot_value":null}]],"number_slot_list":[[{"slot_name":"team_size","slot_type":"attribute","slot_value":"30,>"}]],"pos_list":["ns","n","d","m","n","uj","x","n"],"regionalism_slot_list":[[{"slot_name":"city","slot_type":"attribute","slot_value":"长春市"}]],"segment_list":["长春市","人数","大于","30","人","的","危化","队伍"],"temp_slot_list":[[{"slot_name":"expert_industry","slot_type":"class","slot_value":782261},{"slot_name":"skill","slot_type":"class","slot_value":776103},{"slot_name":"skill","slot_type":"class","slot_value":776117},{"slot_name":"team_type","slot_type":"class","slot_value":776493}]]}
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
         * label_prob : 0.9996978044509888
         * label_type_index : 49
         * processed_query : 长春市人数大于30人的危化队伍
         * _mention_slot_list : [["expert_industry","skill","skill","team_type"],["team"]]
         * _number_slot_list : [["team_size"]]
         * _regionalism_slot_list : [["city"]]
         * _temp_slot_list : [["expert_industry","skill","skill","team_type"]]
         * candidate_mention_list : ["危化","队伍"]
         * location_mention_list : ["长春市"]
         * mention_slot_list : [[{"slot_name":"expert_industry","slot_type":"class","slot_value":782261},{"slot_name":"skill","slot_type":"class","slot_value":776103},{"slot_name":"skill","slot_type":"class","slot_value":776117},{"slot_name":"team_type","slot_type":"class","slot_value":776493}],[{"slot_name":"team","slot_type":"extra","slot_value":null}]]
         * number_slot_list : [[{"slot_name":"team_size","slot_type":"attribute","slot_value":"30,>"}]]
         * pos_list : ["ns","n","d","m","n","uj","x","n"]
         * regionalism_slot_list : [[{"slot_name":"city","slot_type":"attribute","slot_value":"长春市"}]]
         * segment_list : ["长春市","人数","大于","30","人","的","危化","队伍"]
         * temp_slot_list : [[{"slot_name":"expert_industry","slot_type":"class","slot_value":782261},{"slot_name":"skill","slot_type":"class","slot_value":776103},{"slot_name":"skill","slot_type":"class","slot_value":776117},{"slot_name":"team_type","slot_type":"class","slot_value":776493}]]
         */

        private double label_prob;
        private int label_type_index;
        private String processed_query;
        private String  raw_query;
        private List<List<String>> _mention_slot_list;
        private List<List<String>> _number_slot_list;
        private List<List<String>> _regionalism_slot_list;
        private List<List<String>> _temp_slot_list;
        private List<String> candidate_mention_list;
//        private List<String> location_mention_list;
        private List<List<MentionSlotListBean>> mention_slot_list;
        private List<List<NumberSlotListBean>> number_slot_list;
//        private List<String> pos_list;
        private List<List<RegionalismSlotListBean>> regionalism_slot_list;
//        private List<String> segment_list;
        private List<List<TempSlotListBean>> temp_slot_list;


        public String getRaw_query() {
            return raw_query;
        }

        public void setRaw_query(String raw_query) {
            this.raw_query = raw_query;
        }

        public double getLabel_prob() {
            return label_prob;
        }

        public void setLabel_prob(double label_prob) {
            this.label_prob = label_prob;
        }

        public int getLabel_type_index() {
            return label_type_index;
        }

        public void setLabel_type_index(int label_type_index) {
            this.label_type_index = label_type_index;
        }

        public String getProcessed_query() {
            return processed_query;
        }

        public void setProcessed_query(String processed_query) {
            this.processed_query = processed_query;
        }

        public List<List<String>> get_mention_slot_list() {
            return _mention_slot_list;
        }

        public void set_mention_slot_list(List<List<String>> _mention_slot_list) {
            this._mention_slot_list = _mention_slot_list;
        }

        public List<List<String>> get_number_slot_list() {
            return _number_slot_list;
        }

        public void set_number_slot_list(List<List<String>> _number_slot_list) {
            this._number_slot_list = _number_slot_list;
        }

        public List<List<String>> get_regionalism_slot_list() {
            return _regionalism_slot_list;
        }

        public void set_regionalism_slot_list(List<List<String>> _regionalism_slot_list) {
            this._regionalism_slot_list = _regionalism_slot_list;
        }

        public List<List<String>> get_temp_slot_list() {
            return _temp_slot_list;
        }

        public void set_temp_slot_list(List<List<String>> _temp_slot_list) {
            this._temp_slot_list = _temp_slot_list;
        }

        public List<String> getCandidate_mention_list() {
            return candidate_mention_list;
        }

        public void setCandidate_mention_list(List<String> candidate_mention_list) {
            this.candidate_mention_list = candidate_mention_list;
        }

//        public List<String> getLocation_mention_list() {
//            return location_mention_list;
//        }
//
//        public void setLocation_mention_list(List<String> location_mention_list) {
//            this.location_mention_list = location_mention_list;
//        }

        public List<List<MentionSlotListBean>> getMention_slot_list() {
            return mention_slot_list;
        }

        public void setMention_slot_list(List<List<MentionSlotListBean>> mention_slot_list) {
            this.mention_slot_list = mention_slot_list;
        }

        public List<List<NumberSlotListBean>> getNumber_slot_list() {
            return number_slot_list;
        }

        public void setNumber_slot_list(List<List<NumberSlotListBean>> number_slot_list) {
            this.number_slot_list = number_slot_list;
        }

//        public List<String> getPos_list() {
//            return pos_list;
//        }
//
//        public void setPos_list(List<String> pos_list) {
//            this.pos_list = pos_list;
//        }

        public List<List<RegionalismSlotListBean>> getRegionalism_slot_list() {
            return regionalism_slot_list;
        }

        public void setRegionalism_slot_list(List<List<RegionalismSlotListBean>> regionalism_slot_list) {
            this.regionalism_slot_list = regionalism_slot_list;
        }

//        public List<String> getSegment_list() {
//            return segment_list;
//        }
//
//        public void setSegment_list(List<String> segment_list) {
//            this.segment_list = segment_list;
//        }

        public List<List<TempSlotListBean>> getTemp_slot_list() {
            return temp_slot_list;
        }

        public void setTemp_slot_list(List<List<TempSlotListBean>> temp_slot_list) {
            this.temp_slot_list = temp_slot_list;
        }

        public static class MentionSlotListBean {
            /**
             * slot_name : expert_industry
             * slot_type : class
             * slot_value : 782261
             */

            private String slot_name;
            private String slot_type;
            private Object slot_value;

            public String getSlot_name() {
                return slot_name;
            }

            public void setSlot_name(String slot_name) {
                this.slot_name = slot_name;
            }

            public String getSlot_type() {
                return slot_type;
            }

            public void setSlot_type(String slot_type) {
                this.slot_type = slot_type;
            }

            public Object getSlot_value() {
                return slot_value;
            }

            public void setSlot_value(Object slot_value) {
                this.slot_value = slot_value;
            }
        }

        public static class NumberSlotListBean {
            /**
             * slot_name : team_size
             * slot_type : attribute
             * slot_value : 30,>
             */

            private String slot_name;
            private String slot_type;
            private String slot_value;

            public String getSlot_name() {
                return slot_name;
            }

            public void setSlot_name(String slot_name) {
                this.slot_name = slot_name;
            }

            public String getSlot_type() {
                return slot_type;
            }

            public void setSlot_type(String slot_type) {
                this.slot_type = slot_type;
            }

            public String getSlot_value() {
                return slot_value;
            }

            public void setSlot_value(String slot_value) {
                this.slot_value = slot_value;
            }
        }

        public static class RegionalismSlotListBean {
            /**
             * slot_name : city
             * slot_type : attribute
             * slot_value : 长春市
             */

            private String slot_name;
            private String slot_type;
            private String slot_value;

            public String getSlot_name() {
                return slot_name;
            }

            public void setSlot_name(String slot_name) {
                this.slot_name = slot_name;
            }

            public String getSlot_type() {
                return slot_type;
            }

            public void setSlot_type(String slot_type) {
                this.slot_type = slot_type;
            }

            public String getSlot_value() {
                return slot_value;
            }

            public void setSlot_value(String slot_value) {
                this.slot_value = slot_value;
            }
        }

        public static class TempSlotListBean {
            /**
             * slot_name : expert_industry
             * slot_type : class
             * slot_value : 782261
             */

            private String slot_name;
            private String slot_type;
            private Object slot_value;

            public String getSlot_name() {
                return slot_name;
            }

            public void setSlot_name(String slot_name) {
                this.slot_name = slot_name;
            }

            public String getSlot_type() {
                return slot_type;
            }

            public void setSlot_type(String slot_type) {
                this.slot_type = slot_type;
            }

            public Object getSlot_value() {
                return slot_value;
            }

            public void setSlot_value(Object slot_value) {
                this.slot_value = slot_value;
            }
        }
    }
}
