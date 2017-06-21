package org.loxf.metric.common.dto;


import java.util.List;

public class ChartDto extends Common{
    private Long id;

    private String chartId;

    private String createUserId;

    private String updateUserId;

    private String chartName;

    private String chartDesc;

    private String type;

    private Integer state;

    private String createUserName;

    private  String updateUserName;

    private String updateAtStr;

    private String createAtStr;

    private List<QuotaDto> quotaList;

    private List<ChartQuotaRelDto> quotaRelList;

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getUpdateAtStr() {
        return updateAtStr;
    }

    public void setUpdateAtStr(String updateAtStr) {
        this.updateAtStr = updateAtStr;
    }

    public String getCreateAtStr() {
        return createAtStr;
    }

    public void setCreateAtStr(String createAtStr) {
        this.createAtStr = createAtStr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChartId() {
        return chartId;
    }

    public void setChartId(String chartId) {
        this.chartId = chartId == null ? null : chartId.trim();
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName == null ? null : chartName.trim();
    }

    public String getChartDesc() {
        return chartDesc;
    }

    public void setChartDesc(String chartDesc) {
        this.chartDesc = chartDesc == null ? null : chartDesc.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public List<QuotaDto> getQuotaList() {
        return quotaList;
    }

    public void setQuotaList(List<QuotaDto> quotaList) {
        this.quotaList = quotaList;
    }

    public List<ChartQuotaRelDto> getQuotaRelList() {
        return quotaRelList;
    }

    public void setQuotaRelList(List<ChartQuotaRelDto> quotaRelList) {
        this.quotaRelList = quotaRelList;
    }
}