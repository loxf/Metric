package org.loxf.metric.common.constants;

/**
 * Created by hutingting on 2017/7/10.
 */
public enum ResultCodeEnum {
    SUCCESS("200","SUCCESS"),
    PARAM_LACK("501","参数不全"),
    PARAM_ERROR("502","参数校验失败"),
    DATA_NOT_EXIST("503","数据不存在"),
    USER_NOT_EXIST("504","用户不存在"),
    NO_PERMISSION("300","权限不足");

    private String code;
    private String codeMsg;
    ResultCodeEnum(String code, String codeMsg){
        this.code=code;
        this.codeMsg=codeMsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeMsg() {
        return codeMsg;
    }

    public void setCodeMsg(String codeMsg) {
        this.codeMsg = codeMsg;
    }
}
