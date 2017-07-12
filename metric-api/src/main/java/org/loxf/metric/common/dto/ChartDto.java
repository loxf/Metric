package org.loxf.metric.common.dto;

import org.loxf.metric.base.ItemList.QuotaItem;

import java.util.Set;

public class ChartDto extends BaseDto{

    private String chartCode;

    private String chartName;

    private Set<QuotaItem> quotaList;

    private String type;

    private String visibleType;

    private Set<String> visibleList;

    private String chartDim;

    private String state;

    private String uniqueCode;

    public String getChartCode() {
        return chartCode;
    }

    public void setChartCode(String chartCode) {
        this.chartCode = chartCode;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChartDim() {
        return chartDim;
    }

    public void setChartDim(String chartDim) {
        this.chartDim = chartDim;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getVisibleType() {
        return visibleType;
    }

    public void setVisibleType(String visibleType) {
        this.visibleType = visibleType;
    }

    public Set<QuotaItem> getQuotaList() {
        return quotaList;
    }

    public void setQuotaList(Set<QuotaItem> quotaList) {
        this.quotaList = quotaList;
    }

    public Set<String> getVisibleList() {
        return visibleList;
    }

    public void setVisibleList(Set<String> visibleList) {
        this.visibleList = visibleList;
    }
}