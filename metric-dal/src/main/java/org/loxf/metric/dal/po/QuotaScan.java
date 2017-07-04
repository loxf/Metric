package org.loxf.metric.dal.po;

import java.util.Date;

public class QuotaScan extends Common{
    private Long id;

    private String scanId;

    private String quotaId;

    private String boardId;

    private String busiDomain;

    private Integer state;

    private String updateUserId;

    /**
     * 作为查询条件用
     */
    private String quotaName;

    private String  quotaScanShowName;

    private Integer quotaScanType;

    /**
     * 指标维度
     */
    private String quotaDim;

    private Date createdAt;

    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId == null ? null : scanId.trim();
    }

    public String getQuotaId() {
        return quotaId;
    }

    public void setQuotaId(String quotaId) {
        this.quotaId = quotaId == null ? null : quotaId.trim();
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId == null ? null : boardId.trim();
    }

    public String getBusiDomain() {
        return busiDomain;
    }

    public void setBusiDomain(String busiDomain) {
        this.busiDomain = busiDomain == null ? null : busiDomain.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getQuotaName() {
        return quotaName;
    }

    public void setQuotaName(String quotaName) {
        this.quotaName = quotaName;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getQuotaScanShowName() {
        return quotaScanShowName;
    }

    public void setQuotaScanShowName(String quotaScanShowName) {
        this.quotaScanShowName = quotaScanShowName;
    }

    public Integer getQuotaScanType() {
        return quotaScanType;
    }

    public void setQuotaScanType(Integer quotaScanType) {
        this.quotaScanType = quotaScanType;
    }

    public String getQuotaDim() {
        return quotaDim;
    }

    public void setQuotaDim(String quotaDim) {
        this.quotaDim = quotaDim;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}