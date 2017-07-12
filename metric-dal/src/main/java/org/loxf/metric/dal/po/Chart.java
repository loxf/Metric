package org.loxf.metric.dal.po;

import org.loxf.metric.base.ItermList.QuotaItem;

import java.util.Date;
import java.util.List;

public class Chart extends BasePO{

    private String chartCode;

    private String chartName;

    private List<QuotaItem> quotaList;

    private String type;

    private String visibleType;

    private List<String> visibleList;

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

    public List<QuotaItem> getQuotaList() {
        return quotaList;
    }

    public void setQuotaList(List<QuotaItem> quotaList) {
        this.quotaList = quotaList;
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

    public List<String> getVisibleList() {
        return visibleList;
    }

    public void setVisibleList(List<String> visibleList) {
        this.visibleList = visibleList;
    }
}