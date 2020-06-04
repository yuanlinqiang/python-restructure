package com.gsafety.pythonrestructure.python.model;

public enum EquipmentEnmu {
    LEVELTYPE(1,"级别"),
    SIZECODE(2,"尺寸"),
    WASHTENTTYPECODE(3,"洗消帐篷类型"),
    BUCKETVOL(2,"收集桶容积"),
    IFHEAT(2,"是否有加热设施"),
    TENTNUMCODE(2,"洗消帐篷数量"),
    TYPECODE(2,"类型"),
    MEDIUMCODE(2,"检测探测介质种类"),
    LIFT(2,"扬程（米 m）"),
    OUTPUTVOLUME(2,"排量（立方米/时 m3/h）"),
    POWER(2,"功率（千瓦 Kw）"),
    WATERQUACODE(2,"适用水质"),
    APPLSCOPECODE(2,"适用范围"),
    UPABILITY(2,"提升能力（吨 t）"),
    DRYPOWDERTYPECODE(2,"干粉类型"),
    DRYPOWDERCODE(2,"干粉量"),
    MAXRANGECODE(2,"最大射程"),
    BROKENCODE(2,"破拆器材数量"),
    PLUGGCODE(2,"堵漏器材数量"),
    FIREEXITCODE(2,"灭火设备"),
    GASBOTTLECODE(2,"同时充气气瓶数量"),
    FOAMNUMCODE(2,"携带泡沫量"),
    MAXHEGHTCODE(2,"最大举高高度"),
    VEHICLECODE(2,"车载炮流量"),
    WATERVOLCODE(2,"水量"),
    CARTYPECODE(2,"车辆类型"),
    FOAMTYPECODE(2,"泡沫类型"),
    KINDCODE(2,"种类"),
    TYPE(2,"类型"),
    FOAMKINDCODE(2,"泡沫种类"),
    SPEEDCODE(2,"补给速度"),
    DISTANCECODE(2,"供水距离"),
    MAXFLOWCODE(2,"最大供水流量"),
    MODECODE(2,"支撑方式"),
    FLOWCODE(2,"流量"),
    DRYPOWNUMCODE(2,"携带超细干粉量"),
    FOAMRATIOCODE(2,"泡沫混合比例"),
    IPRESSCODE(2,"是否自带加压"),
    SUPPMODECODE(2,"供水方式"),
    CARPEOCODE(2,"载人数"),
    STRUCTTYPECODE(2,"结构类型"),
    POWERCODE(2,"功率"),
    ENGINECODE(2,"发动机"),
    BATREADYCODE(2,"战备状态"),
    INTELLIGENTCONCODE(2,"智能控制"),
    CARRYWATERCODE(2,"最大吊桶载水量"),
    FLYWEIGHTCODE(2,"起飞全重"),
    LARCARPEOCODE(2,"最大载人数"),
    FULLW(2,"起飞全重"),
    MAXPEONUM(2,"最大载人数"),
    MAXWATERNUM(2,"最大吊桶载水量");

    private int code;
    private String msg;


    private EquipmentEnmu(int ordinal, String name) {
        this.code = ordinal;
        this.msg = name;
    }
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

}
