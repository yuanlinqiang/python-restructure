package com.gsafety.pythonrestructure.python.model;

public class Knowledge {

    /**
     * body : {"chemical_property":"白色晶体粉末或颗粒。溶于水、乙醇,微溶于丙酮,不溶于苯、乙醚。分子量122.11,沸点212-217℃,低于沸点分解,相对密度(水=1)。","emergency_response":{"extinguish_media":"用水灭火。禁止使用砂土、干粉灭火。","extinguishing_method":"总体描述：大火时，远距离用大量水灭火。消防人员应佩戴防毒面具、穿全身消防服，在上风向灭火。在确保安全的前提下将容器移离火场。切勿开动已处于火场中的货船或车辆。筑堤收容消防废水。 如果在火场中有储罐、槽车或罐车，周围至少隔离800米；同时初始疏散距离也至少为800米。<br />储罐/公路/铁路槽车火灾：------<br />储罐火灾：------","first_aid":"吸入：迅速脱离现场至空气新鲜处，休息。就医。<br />眼睛接触：立即提起眼睑，用流动清水或生理盐水冲洗。就医。<br />皮肤接触：立即用大量水冲洗，然后脱去污染的衣着，接着再冲洗，就医。<br />食入：饮足量温水，不要催吐。就医。<br />","leak_emergency":"总体描述：隔离泄漏污染区，限制出入。消除所有点火源（泄漏区附近禁止吸烟、消除所有明火、火花或火焰）。建议应急处理人员戴防尘面具（全面罩），穿防毒服。不要直接接触泄漏物。勿使泄漏物与有机物、还原剂、易燃物接触。防止泄漏物进入水体、下水道、地下室或密闭空间。小量泄漏：用大量水冲洗泄漏区。大量泄漏：在专业人员指导下清除。<br />固体泄漏：------<br />水体泄漏：------<br />粉末泄漏：------<br />溶液泄漏：------"},"hazard_info":{"active_reaction":"强氧化剂，与硝基化合物和氯酸盐组成的混合物对振动和摩擦敏感并可能爆炸。","dangers":"受热、接触明火、或受到摩擦、震动、撞击时可发生爆炸。加热至150℃ 时分解并爆炸。","health_hazards":"侵入途径：------<br />中毒表现：对眼睛、皮肤、黏膜和呼吸道有刺激性。<br />解毒剂：------"},"special_warning":"加热至150℃ 时分解并爆炸。"}
     * record_id : 35201
     * title : 硝酸胍
     */

    private BodyBean body;
    private String record_id;
    private String title;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
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

    public static class BodyBean {
        /**
         * chemical_property : 白色晶体粉末或颗粒。溶于水、乙醇,微溶于丙酮,不溶于苯、乙醚。分子量122.11,沸点212-217℃,低于沸点分解,相对密度(水=1)。
         * emergency_response : {"extinguish_media":"用水灭火。禁止使用砂土、干粉灭火。","extinguishing_method":"总体描述：大火时，远距离用大量水灭火。消防人员应佩戴防毒面具、穿全身消防服，在上风向灭火。在确保安全的前提下将容器移离火场。切勿开动已处于火场中的货船或车辆。筑堤收容消防废水。 如果在火场中有储罐、槽车或罐车，周围至少隔离800米；同时初始疏散距离也至少为800米。<br />储罐/公路/铁路槽车火灾：------<br />储罐火灾：------","first_aid":"吸入：迅速脱离现场至空气新鲜处，休息。就医。<br />眼睛接触：立即提起眼睑，用流动清水或生理盐水冲洗。就医。<br />皮肤接触：立即用大量水冲洗，然后脱去污染的衣着，接着再冲洗，就医。<br />食入：饮足量温水，不要催吐。就医。<br />","leak_emergency":"总体描述：隔离泄漏污染区，限制出入。消除所有点火源（泄漏区附近禁止吸烟、消除所有明火、火花或火焰）。建议应急处理人员戴防尘面具（全面罩），穿防毒服。不要直接接触泄漏物。勿使泄漏物与有机物、还原剂、易燃物接触。防止泄漏物进入水体、下水道、地下室或密闭空间。小量泄漏：用大量水冲洗泄漏区。大量泄漏：在专业人员指导下清除。<br />固体泄漏：------<br />水体泄漏：------<br />粉末泄漏：------<br />溶液泄漏：------"}
         * hazard_info : {"active_reaction":"强氧化剂，与硝基化合物和氯酸盐组成的混合物对振动和摩擦敏感并可能爆炸。","dangers":"受热、接触明火、或受到摩擦、震动、撞击时可发生爆炸。加热至150℃ 时分解并爆炸。","health_hazards":"侵入途径：------<br />中毒表现：对眼睛、皮肤、黏膜和呼吸道有刺激性。<br />解毒剂：------"}
         * special_warning : 加热至150℃ 时分解并爆炸。
         */

        private String chemical_property;
        private EmergencyResponseBean emergency_response;
        private HazardInfoBean hazard_info;
        private String special_warning;

        public String getChemical_property() {
            return chemical_property;
        }

        public void setChemical_property(String chemical_property) {
            this.chemical_property = chemical_property;
        }

        public EmergencyResponseBean getEmergency_response() {
            return emergency_response;
        }

        public void setEmergency_response(EmergencyResponseBean emergency_response) {
            this.emergency_response = emergency_response;
        }

        public HazardInfoBean getHazard_info() {
            return hazard_info;
        }

        public void setHazard_info(HazardInfoBean hazard_info) {
            this.hazard_info = hazard_info;
        }

        public String getSpecial_warning() {
            return special_warning;
        }

        public void setSpecial_warning(String special_warning) {
            this.special_warning = special_warning;
        }

        public static class EmergencyResponseBean {
            /**
             * extinguish_media : 用水灭火。禁止使用砂土、干粉灭火。
             * extinguishing_method : 总体描述：大火时，远距离用大量水灭火。消防人员应佩戴防毒面具、穿全身消防服，在上风向灭火。在确保安全的前提下将容器移离火场。切勿开动已处于火场中的货船或车辆。筑堤收容消防废水。 如果在火场中有储罐、槽车或罐车，周围至少隔离800米；同时初始疏散距离也至少为800米。<br />储罐/公路/铁路槽车火灾：------<br />储罐火灾：------
             * first_aid : 吸入：迅速脱离现场至空气新鲜处，休息。就医。<br />眼睛接触：立即提起眼睑，用流动清水或生理盐水冲洗。就医。<br />皮肤接触：立即用大量水冲洗，然后脱去污染的衣着，接着再冲洗，就医。<br />食入：饮足量温水，不要催吐。就医。<br />
             * leak_emergency : 总体描述：隔离泄漏污染区，限制出入。消除所有点火源（泄漏区附近禁止吸烟、消除所有明火、火花或火焰）。建议应急处理人员戴防尘面具（全面罩），穿防毒服。不要直接接触泄漏物。勿使泄漏物与有机物、还原剂、易燃物接触。防止泄漏物进入水体、下水道、地下室或密闭空间。小量泄漏：用大量水冲洗泄漏区。大量泄漏：在专业人员指导下清除。<br />固体泄漏：------<br />水体泄漏：------<br />粉末泄漏：------<br />溶液泄漏：------
             */

            private String extinguish_media;
            private String extinguishing_method;
            private String first_aid;
            private String leak_emergency;

            public String getExtinguish_media() {
                return extinguish_media;
            }

            public void setExtinguish_media(String extinguish_media) {
                this.extinguish_media = extinguish_media;
            }

            public String getExtinguishing_method() {
                return extinguishing_method;
            }

            public void setExtinguishing_method(String extinguishing_method) {
                this.extinguishing_method = extinguishing_method;
            }

            public String getFirst_aid() {
                return first_aid;
            }

            public void setFirst_aid(String first_aid) {
                this.first_aid = first_aid;
            }

            public String getLeak_emergency() {
                return leak_emergency;
            }

            public void setLeak_emergency(String leak_emergency) {
                this.leak_emergency = leak_emergency;
            }
        }

        public static class HazardInfoBean {
            /**
             * active_reaction : 强氧化剂，与硝基化合物和氯酸盐组成的混合物对振动和摩擦敏感并可能爆炸。
             * dangers : 受热、接触明火、或受到摩擦、震动、撞击时可发生爆炸。加热至150℃ 时分解并爆炸。
             * health_hazards : 侵入途径：------<br />中毒表现：对眼睛、皮肤、黏膜和呼吸道有刺激性。<br />解毒剂：------
             */

            private String active_reaction;
            private String dangers;
            private String health_hazards;

            public String getActive_reaction() {
                return active_reaction;
            }

            public void setActive_reaction(String active_reaction) {
                this.active_reaction = active_reaction;
            }

            public String getDangers() {
                return dangers;
            }

            public void setDangers(String dangers) {
                this.dangers = dangers;
            }

            public String getHealth_hazards() {
                return health_hazards;
            }

            public void setHealth_hazards(String health_hazards) {
                this.health_hazards = health_hazards;
            }
        }
    }
}
