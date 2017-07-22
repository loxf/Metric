package org.loxf.metric.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by luohj on 2017/7/22.
 */
@ApiModel("目标数据")
public class TargetData implements Serializable {
    private String targetName;
    private Date targetStartTime;
    private Date targetEndTime;
    private BigDecimal percent;
    private BigDecimal trend;
    private List<TargetQuotaData> itemList;

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Date getTargetStartTime() {
        return targetStartTime;
    }

    public void setTargetStartTime(Date targetStartTime) {
        this.targetStartTime = targetStartTime;
    }

    public Date getTargetEndTime() {
        return targetEndTime;
    }

    public void setTargetEndTime(Date targetEndTime) {
        this.targetEndTime = targetEndTime;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public BigDecimal getTrend() {
        return trend;
    }

    public void setTrend(BigDecimal trend) {
        this.trend = trend;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public List<TargetQuotaData> getItemList() {
        return itemList;
    }

    public void setItemList(List<TargetQuotaData> itemList) {
        this.itemList = itemList;
    }
}
