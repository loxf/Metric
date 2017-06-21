package org.loxf.metric.common.constants;

/**
 * Created by luohj on 2017/5/9.
 */
public enum QuotaType {
    BASIC("BASIC", "基础指标"),
    COMPOSITE("COMPOSITE","复合指标");

    QuotaType(String value, String desc){
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
