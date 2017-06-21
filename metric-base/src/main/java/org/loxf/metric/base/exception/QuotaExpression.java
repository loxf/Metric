package org.loxf.metric.base.exception;

/**
 * 指标运算异常
 * Created by luohj on 2017/5/9.
 */
public class QuotaExpression extends RuntimeException {
    private String code ;
    private String name ;
    public QuotaExpression(Exception e){
        super(e);
    }
    public QuotaExpression(String name, Exception e){
        super(name, e);
        this.name = name;
    }
    public QuotaExpression(String name){
        super(name);
        this.name = name;
    }
    public QuotaExpression(String name, String code){
        super(code+":"+name);
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

