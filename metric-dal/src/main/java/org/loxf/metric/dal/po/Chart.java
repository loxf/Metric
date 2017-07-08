package org.loxf.metric.dal.po;

import org.loxf.metric.base.ItermList.QuotaItem;

import java.util.Date;
import java.util.List;

public class Chart extends BasePO{

    private String chartCode;

    private String chartName;

    private List<QuotaItem> quotaList;

    private String type;

    private String chartDim;

    private Integer state;

    private String createUserName;

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

}