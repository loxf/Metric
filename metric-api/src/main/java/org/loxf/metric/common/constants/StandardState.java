package org.loxf.metric.common.constants;

/**
 * Created by luohj on 2017/5/4.
 */
public enum StandardState {
    AVAILABLE(1, "生效"),
    DISABLED(0, "失效");
    StandardState(int value, String desc){
        this.value = value;
        this.desc = desc;
    }
    private int value;
    private String desc;

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
    public static String getDesc(int state){
        if(AVAILABLE.value==state){
            return AVAILABLE.desc;
        } else if(DISABLED.value==state){
            return DISABLED.desc;
        } else {
            return null;
        }
    }
}
