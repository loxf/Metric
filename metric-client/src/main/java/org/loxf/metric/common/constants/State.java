package org.loxf.metric.common.constants;

/**
 * Created by luohj on 2017/5/4.
 */
public enum State {
    EFF(1, "生效"),
    EXP(0, "失效");
    State(int value, String desc){
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
        if(EFF.value==state){
            return EFF.desc;
        } else if(EXP.value==state){
            return EXP.desc;
        } else {
            return null;
        }
    }
}
