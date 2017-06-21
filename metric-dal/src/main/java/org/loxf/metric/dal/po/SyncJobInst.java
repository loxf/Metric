package org.loxf.metric.dal.po;

import java.util.Date;

public class SyncJobInst extends Common {
    private Long id;

    private String jobCode;

    private String jobType;

    private String jobName;

    private Integer particleSize;

    private Date circleTime;

    private Integer state;

    private Integer total;

    private Integer syncNbr;

    private Date runDate;

    private Date completeDate;

    private String remark;

    private Date createdAt;

    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getParticleSize() {
        return particleSize;
    }

    public void setParticleSize(Integer particleSize) {
        this.particleSize = particleSize;
    }

    public Date getCircleTime() {
        return circleTime;
    }

    public void setCircleTime(Date circleTime) {
        this.circleTime = circleTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getSyncNbr() {
        return syncNbr;
    }

    public void setSyncNbr(Integer syncNbr) {
        this.syncNbr = syncNbr;
    }

    public Date getRunDate() {
        return runDate;
    }

    public void setRunDate(Date runDate) {
        this.runDate = runDate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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