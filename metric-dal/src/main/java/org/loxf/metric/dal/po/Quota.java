package org.loxf.metric.dal.po;

import org.loxf.metric.base.ItemList.QuotaDimItem;

import java.util.List;

public class Quota extends BasePO {
    private String quotaCode;

    private String quotaSource;

    private String quotaName;

    private String type;

    private String showOperation;

    private String showType;

    private String state;

    private List<QuotaDimItem> quotaDim;

    private String dataImportType;

    private int intervalPeriod;

    private String uniqueCode;

    public String getQuotaCode() {
        return quotaCode;
    }

    public void setQuotaCode(String quotaCode) {
        this.quotaCode = quotaCode;
    }

    public String getQuotaSource() {
        return quotaSource;
    }

    public void setQuotaSource(String quotaSource) {
        this.quotaSource = quotaSource;
    }

    public String getQuotaName() {
        return quotaName;
    }

    public void setQuotaName(String quotaName) {
        this.quotaName = quotaName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShowOperation() {
        return showOperation;
    }

    public void setShowOperation(String showOperation) {
        this.showOperation = showOperation;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<QuotaDimItem> getQuotaDim() {
        return quotaDim;
    }

    public void setQuotaDim(List<QuotaDimItem> quotaDim) {
        this.quotaDim = quotaDim;
    }

    public String getDataImportType() {
        return dataImportType;
    }

    public void setDataImportType(String dataImportType) {
        this.dataImportType = dataImportType;
    }

    public int getIntervalPeriod() {
        return intervalPeriod;
    }

    public void setIntervalPeriod(int intervalPeriod) {
        this.intervalPeriod = intervalPeriod;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}