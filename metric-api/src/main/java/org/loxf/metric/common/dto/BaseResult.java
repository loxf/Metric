package org.loxf.metric.common.dto;


import org.loxf.metric.common.constants.ResultCodeEnum;

import java.io.Serializable;


/**
 * Created by wugang on 2017/2/23.
 */
public class BaseResult<T> implements Serializable {

    /**
     * 信息反馈
     */
    protected String code;
    protected String msg;
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
