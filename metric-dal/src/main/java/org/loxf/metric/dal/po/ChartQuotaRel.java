package org.loxf.metric.dal.po;

import java.util.Date;

public class ChartQuotaRel {
    private Long id;

    private String chartId;

    private String quotaId;

    private String quotaDimension;

    private Date createdAt;

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

    public String getQuotaId() {
        return quotaId;
    }

    public void setQuotaId(String quotaId) {
        this.quotaId = quotaId == null ? null : quotaId.trim();
    }

    public String getQuotaDimension() {
        return quotaDimension;
    }

    public void setQuotaDimension(String quotaDimension) {
        this.quotaDimension = quotaDimension;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}