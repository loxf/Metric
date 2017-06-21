package org.loxf.metric.common.dto;


import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Created by luohj on 2017/5/10.
 */
public class Condition implements Serializable {
    private String code;
    private String desc;
    private String[] value;
    private String oper;

    public Condition(){}

    public Condition(String code, String[] value) {
        this.code = code;
        this.value = value;
    }

    public Condition(String code, String oper, String[] value) {
        this.code = code;
        this.oper = oper;
        this.value = value;
    }

    public Condition(String code, String[] value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String[] getValue() {
        return value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public String toString() {
        String str = code + oper ;
        if(value!=null) {
            str += "(";
            str += "'" + StringUtils.join(value, "','") + "'";
            str += ")";
        }
        return str;
    }

}
