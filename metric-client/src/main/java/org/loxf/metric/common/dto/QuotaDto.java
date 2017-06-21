package org.loxf.metric.common.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by luohj on 2017/5/4.
 */
public class QuotaDto extends Common {
    private Long id;

    private String quotaId;

    private String quotaCode;

    private String quotaSource;

    private String expression;

    private String quotaName;

    private String quotaDisplayName;

    private Integer particleSize;

    private String cron;

    private String type;

    private String showOperation;

    private Integer isInit;

    private Date firstRunTime;

    private Integer state;

    private Byte isDeleted;

    private Date createdAt;

    private Date updatedAt;

    private String quotaDim;

    private String chartDimension;

    private List<QuotaDimensionDto> dimensionList;

    private String updateUserId;

    private String createUserId;

    private String updateUserName;

    private String createUserName;

    private String showType;

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
        this.quotaId = quotaId;
    }

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
        this.quotaName = quotaName;
    }

    public String getQuotaDisplayName() {
        return quotaDisplayName;
    }

    public void setQuotaDisplayName(String quotaDisplayName) {
        this.quotaDisplayName = quotaDisplayName;
    }

    public Integer getParticleSize() {
        return particleSize;
    }

    public void setParticleSize(Integer particleSize) {
        this.particleSize = particleSize;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
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

    public Integer getIsInit() {
        return isInit;
    }

    public void setIsInit(Integer isInit) {
        this.isInit = isInit;
    }

    public Date getFirstRunTime() {
        return firstRunTime;
    }

    public void setFirstRunTime(Date firstRunTime) {
        this.firstRunTime = firstRunTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public String getQuotaDim() {
        return quotaDim;
    }

    public void setQuotaDim(String quotaDim) {
        this.quotaDim = quotaDim;
    }

    public String getChartDimension() {
        return chartDimension;
    }

    public void setChartDimension(String chartDimension) {
        this.chartDimension = chartDimension;
    }

    public List<QuotaDimensionDto> getDimensionList() {
        return dimensionList;
    }

    public void setDimensionList(List<QuotaDimensionDto> dimensionList) {
        this.dimensionList = dimensionList;
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
