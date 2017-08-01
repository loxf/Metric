package org.loxf.metric.base.ItemList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by hutingting on 2017/7/6.
 */
@ApiModel("目标项")
public class TargetItem implements Serializable {

    @ApiModelProperty(value = "指标编码", required = true)
    private String quotaCode;

    @ApiModelProperty(value = "指标名称")
    private String quotaName;

    @ApiModelProperty(value = "目标值", required = true)
    private String targetValue;

    @ApiModelProperty(value = "权重", required = true)
    private int weight;

    @ApiModelProperty(value = "指标计算方法，默认继承指标")
    private String summary;

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
