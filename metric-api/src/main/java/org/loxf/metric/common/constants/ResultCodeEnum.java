package org.loxf.metric.common.constants;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by hutingting on 2017/7/10.
 */
public enum ResultCodeEnum {
    SUCCESS("200","SUCCESS"),
    PARAM_LACK("501","参数不能为空"),
    PARAM_ERROR("502","参数校验失败"),
    DATA_NOT_EXIST("503","没有数据"),
    USER_NOT_EXIST("504","用户不存在"),
    DATA_EXIST("505","数据已存在"),
    NOT_SUPPORT("509","接口弃用"),
    NO_PERMISSION("300","权限不足"),
    ILLEGAL_REQUEST("555","非法请求"),;

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
