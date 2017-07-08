package org.loxf.metric.base.ItermList;

/**
 * Created by hutingting on 2017/7/6.
 */
public class TargetItem {

    private String quotaCode;

    private String quotaName;

    private String targetValue;

    private int weight;

    public String getQuotaCode() {
        return quotaCode;
    }

    public void setQuotaCode(String quotaCode) {
        this.quotaCode = quotaCode;
    }

    public String getQuotaName() {
        return quotaName;
    }

    public void setQuotaName(String quotaName) {
        this.quotaName = quotaName;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
