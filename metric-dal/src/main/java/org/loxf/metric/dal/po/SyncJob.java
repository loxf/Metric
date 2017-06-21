package org.loxf.metric.dal.po;

import java.util.Date;

public class SyncJob extends Common {
    private Integer id;

    private String jobCode;

    private String jobType;

    private String jobName;

    private String cron;

    private Integer particleSize;

    private String clazz;

    private String dataOperClass;

    private Integer status;

    private Date lastCircleTime;

    private Date runLastTime;

    private Date firstRunTime;

    private Integer isInit;

    private String dataTable;

    private Date createdAt;

    private Date updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode == null ? null : jobCode.trim();
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType == null ? null : jobType.trim();
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron == null ? null : cron.trim();
    }

    public Integer getParticleSize() {
        return particleSize;
    }

    public void setParticleSize(Integer particleSize) {
        this.particleSize = particleSize;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz == null ? null : clazz.trim();
    }

    public String getDataOperClass() {
        return dataOperClass;
    }

    public void setDataOperClass(String dataOperClass) {
        this.dataOperClass = dataOperClass;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLastCircleTime() {
        return lastCircleTime;
    }

    public void setLastCircleTime(Date lastCircleTime) {
        this.lastCircleTime = lastCircleTime;
    }

    public Date getRunLastTime() {
        return runLastTime;
    }

    public void setRunLastTime(Date runLastTime) {
        this.runLastTime = runLastTime;
    }

    public Date getFirstRunTime() {
        return firstRunTime;
    }

    public void setFirstRunTime(Date firstRunTime) {
        this.firstRunTime = firstRunTime;
    }

    public Integer getIsInit() {
        return isInit;
    }

    public void setIsInit(Integer isInit) {
        this.isInit = isInit;
    }

    public String getDataTable() {
        return dataTable;
    }

    public void setDataTable(String dataTable) {
        this.dataTable = dataTable == null ? null : dataTable.trim();
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