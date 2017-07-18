package org.loxf.metric.common.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.loxf.metric.common.constants.ResultCodeEnum;

import java.io.Serializable;

@ApiModel(value = "返回消息")
public class BaseResult<T> implements Serializable {

    @ApiModelProperty(value = "编码", example = "200代表成功，其他失败", position = 1)
    protected String code;
    @ApiModelProperty(value = "消息", example = "返回的消息", position = 2)
    protected String msg;
    @ApiModelProperty(value = "数据", example = "返回的数据，失败时为空", position = 3)
    protected T data;

    public BaseResult() {
        this.code = ResultCodeEnum.SUCCESS.getCode();
        this.msg = ResultCodeEnum.SUCCESS.getCodeMsg();
    }

    public BaseResult(T data) {
        this.code = ResultCodeEnum.SUCCESS.getCode();
        this.msg = ResultCodeEnum.SUCCESS.getCodeMsg();
        this.data = data;
    }

    public BaseResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
