package org.loxf.metric.dal.po;

import java.util.Date;
import java.util.List;

public class Quota extends Common {
    private Long id;

    private String quotaId;

    private String quotaCode;

    private String quotaSource;

    private String expression;

    private String quotaName;

    private String quotaDisplayName;

    private Integer particleSize;

    private String type;

    private String showOperation;

    private Integer state;

    private String quotaDim;

    private Byte isDeleted;

    private Date createdAt;

    private Date updatedAt;

    private String updateUserId;

    private String createUserId;

    private String updateUserName;

    private String createUserName;

    private String showType;

    private List<QuotaDimension> quotaDimensionList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuotaId() {
        return quotaId;
    }

    public void setQuotaId(String quotaId) {
        this.quotaId = quotaId == null ? null : quotaId.trim();
    }

    public String getQuotaCode() {
        return quotaCode;
    }

    public void setQuotaCode(String quotaCode) {
        this.quotaCode = quotaCode == null ? null : quotaCode.trim();
    }

    public String getQuotaSource() {
        return quotaSource;
    }

    public void setQuotaSource(String quotaSource) {
        this.quotaSource = quotaSource == null ? null : quotaSource.trim();
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getQuotaName() {
        return quotaName;
    }

    public void setQuotaName(String quotaName) {
        this.quotaName = quotaName == null ? null : quotaName.trim();
    }

    public String getQuotaDisplayName() {
        return quotaDisplayName;
    }

    public void setQuotaDisplayName(String quotaDisplayName) {
        this.quotaDisplayName = quotaDisplayName == null ? null : quotaDisplayName.trim();
    }

    public Integer getParticleSize() {
        return particleSize;
    }

    public void setParticleSize(Integer particleSize) {
        this.particleSize = particleSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getShowOperation() {
        return showOperation;
    }

    public void setShowOperation(String showOperation) {
        this.showOperation = showOperation == null ? null : showOperation.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getQuotaDim() {
        return quotaDim;
    }

    public void setQuotaDim(String quotaDim) {
        this.quotaDim = quotaDim;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
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

    public List<QuotaDimension> getQuotaDimensionList() {
        return quotaDimensionList;
    }

    public void setQuotaDimensionList(List<QuotaDimension> quotaDimensionList) {
        this.quotaDimensionList = quotaDimensionList;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
}