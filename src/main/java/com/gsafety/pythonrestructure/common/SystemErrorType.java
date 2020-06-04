package com.gsafety.pythonrestructure.common;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
@Getter
public enum SystemErrorType implements ErrorType {




    UNSPECIFIED("500", "网络异常，请稍后再试"),
    NO_SERVICE("404", "网络异常, 服务器熔断"),


    SYSTEM_ERROR("-1", "系统异常"),
    SYSTEM_BUSY("000001", "系统繁忙,请稍候再试"),

    GATEWAY_NOT_FOUND_SERVICE("010404", "服务未找到"),
    GATEWAY_ERROR("010500", "网关异常"),
    GATEWAY_CONNECT_TIME_OUT("010002", "网关超时"),

    ARGUMENT_NOT_VALID("020000", "请求参数校验不通过"),
    INVALID_TOKEN("020001", "无效token"),
    UPLOAD_FILE_SIZE_LIMIT("020010", "上传文件大小超过限制"),
    /** OAuth2z自定义异常信息 */
    NO400000("400000", "请求错误"),
    NO400001("400001", "请求参数必填"),
    NO400002("400002", "请求方法类型不支持"),
    NO400003("400003", "参数类型不匹配"),
    NO400004("400004", "参数验证失败"),
    NO400005("400005", "参数转化错误"),
    NO400006("400006", "请求路径错误"),
    NO501001("501001", "用户不存在"),
    NO501002("501002", "用户已删除"),
    NO501003("501003", "用户不可用"),
    NO501004("501004", "用户已存在"),
    NO501005("501005", "原始密码不正确"),
    NO501006("501006", "用户不能删除自己"),
    NO501009("501009", "用户状态正常"),
    NO501010("501010", "用户不存在可访问权限点"),
    NO502005("502005", "父节点错误"),
    NO503001("503001", "角色已存在"),
    NO503002("503002", "角色不存在"),
    NO504002("504002", "该菜单已存在"),
    NO504006("504006", "该方法属于树形权限点接口"),
    NO505003("505003", "无效的scope"),
    NO507010("507010", "该用户组已存在"),
    NO506001("506001", "部分数据错误"),
    NO506002("506002", "全部数据错误"),
    NO700002("700002", "用户不属于业务系统"),
    NO700004("700004", "无权访问此权限点"),
    NO701001("701001", "用户认证失败"),
    NO701002("701002", "授权码不合法"),

    NO702000("702000", "OAUTH异常"),
    unauthorized("unauthorized", "请求头数据格式不对"),
    NO902000("902000", "参数校验异常"),
    DUPLICATE_PRIMARY_KEY("030000","唯一键冲突");


    /**
     * 错误类型码
     */
    private String code;
    /**
     * 错误类型描述信息
     */
    private String mesg;

    SystemErrorType(String code, String mesg) {
        this.code = code;
        this.mesg = mesg;
    }



    /**
     * 根据编码查询枚举。
     *
     * @param code 编码。
     * @return 枚举。
     */
    public static SystemErrorType getByCode(String code) {
        for (SystemErrorType value : SystemErrorType.values()) {
            if (StringUtils.equals(code, value.getCode())) {
                return value;
            }
        }
         return UNSPECIFIED;
    }

    /**
     * 枚举是否包含此code
     * @param code 枚举code
     * @return 结果
     */
    public static Boolean contains(String code){
        for (SystemErrorType value : SystemErrorType.values()) {
            if (StringUtils.equals(code, value.getCode())) {
                return true;
            }
        }
        return  false;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMesg() {
        return mesg;
    }
}
