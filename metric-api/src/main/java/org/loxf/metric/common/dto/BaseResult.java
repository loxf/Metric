package org.loxf.metric.common.dto;


import java.io.Serializable;


/**
 * Created by wugang on 2017/2/23.
 */
public class BaseResult<T> implements Serializable {
    /**
     * 状态标示
     */
    public static final int FAILED = 0;
    public static final int SUCCESS = 1;

    /**
     * 信息反馈
     */
    public static final String SUCCESS_MSG = "操作成功!";
    public static final String FAILED_MSG = "操作失败!";
    protected int code;
    protected String msg;
    protected T data;

    public BaseResult() {
        this.code = SUCCESS;
        this.msg = SUCCESS_MSG;
    }

    public BaseResult(T data) {
        this.code = SUCCESS;
        this.msg = SUCCESS_MSG;
        this.data = data;
    }

    public BaseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResult(int code, String msg, T data) {
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
