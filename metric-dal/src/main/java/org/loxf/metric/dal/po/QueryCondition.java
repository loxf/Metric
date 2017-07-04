package org.loxf.metric.dal.po;

/**
 * Created by luohj on 2017/6/26.
 */
public class QueryCondition {
    private String code;
    private String[] value;
    private String oper;

    public QueryCondition(String code, String oper, String[] value) {
        this.code = code;
        this.oper = oper;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
}
