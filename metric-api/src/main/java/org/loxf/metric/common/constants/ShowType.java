package org.loxf.metric.common.constants;

/**
 * 展示类型
 * Created by luohj on 2017/5/9.
 */
public enum ShowType {
    /**
     * 精度为2位
     */
    MONEY("MONEY", "金额"),
    /**
     * 精度为0
     */
    NUMBER("NUMBER","数字"),
    /**
     * 比率*100，精度2位
     */
    PERCENT("PERCENT","百分比"),
    /**
     * 精度3位
     */
    RATIO("RATIO","比率");

    ShowType(String value, String desc){
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
}
