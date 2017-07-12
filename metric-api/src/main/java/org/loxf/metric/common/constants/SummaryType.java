package org.loxf.metric.common.constants;

/**
 * 聚合类型
 * Created by luohj on 2017/7/12.
 */
public enum SummaryType {
    SUM("合计"),
    AVG("平均值"),
    MAX("最大值"),
    MIN("最小值"),
    COUNT("计数");

    SummaryType(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;
}
