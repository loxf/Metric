package org.loxf.metric.base.constants;

/**
 * Created by luohj on 2017/5/4.
 */
public enum StandardState {
    AVAILABLE("AVAILABLE", "生效"),
    DISABLED("AVAILABLE", "失效");
    StandardState(String value, String desc){
        this.value = value;
        this.desc = desc;
    }
    private String value;
    private String desc;

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
    public static String getDesc(String state){
        if(AVAILABLE.value.equals(state)){
            return AVAILABLE.desc;
        } else if(DISABLED.value.equals(state)){
            return DISABLED.desc;
        } else {
            return null;
        }
    }
}
