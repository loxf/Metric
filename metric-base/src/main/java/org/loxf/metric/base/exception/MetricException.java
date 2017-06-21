package org.loxf.metric.base.exception;

/**
 * Created by luohj on 2017/5/4.
 */
public class MetricException extends RuntimeException {
    private String code ;
    private String name ;
    public MetricException(Exception e){
        super(e);
    }
    public MetricException(String name, Exception e){
        super(name, e);
    }
    public MetricException(String name){
        super(name);
    }
    public MetricException(String name, String code){
        super(code+":"+name);
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }
}
