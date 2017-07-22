package org.loxf.metric.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by luohj on 2017/7/21.
 */
@ApiModel("目标的指标数据")
public class TargetQuotaData implements Serializable {
    @ApiModelProperty(value = "指标名称", required = true)
    private String quotaName;
    @ApiModelProperty(value = "当前值", required = true)
    private BigDecimal currentValue;
    @ApiModelProperty(value = "目标值", required = true)
    private BigDecimal targetValue;
    @ApiModelProperty(value = "百分比", required = true)
    private BigDecimal percent;
    /**
     * 是否提前 1：提前 0：持平 -1 延后
     */
    @ApiModelProperty(value = "进度： 1：提前 0：持平 -1 延后", required = true)
    private int isAdvance;

    public String getQuotaName() {
        return quotaName;
    }

    public void setQuotaName(String quotaName) {
        this.quotaName = quotaName;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(BigDecimal targetValue) {
        this.targetValue = targetValue;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public int getIsAdvance() {
        return isAdvance;
    }

    public void setIsAdvance(int isAdvance) {
        this.isAdvance = isAdvance;
    }
}
