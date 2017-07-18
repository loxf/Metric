package org.loxf.metric.base.ItemList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by hutingting on 2017/7/6.
 */
@ApiModel("目标项")
public class TargetItem {

    @ApiModelProperty("指标编码")
    private String quotaCode;

    @ApiModelProperty("指标名称")
    private String quotaName;

    @ApiModelProperty("目标值")
    private String targetValue;

    @ApiModelProperty("权重")
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
