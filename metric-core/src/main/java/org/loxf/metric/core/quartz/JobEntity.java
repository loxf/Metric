package org.loxf.metric.core.quartz;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by luohj on 2017/5/4.
 */
public class JobEntity implements Serializable {
    private String jobId;
    private String jobType;
    private String jobName;
    private String cron;
    private String clazz;
    private String methods;
    private int status;
    private Date runLastTime;
    private Date runNexTime;
    private Date syncBeginTime;
    private Date syncEndTime;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getRunLastTime() {
        return runLastTime;
    }

    public void setRunLastTime(Date runLastTime) {
        this.runLastTime = runLastTime;
    }

    public Date getRunNexTime() {
        return runNexTime;
    }

    public void setRunNexTime(Date runNexTime) {
        this.runNexTime = runNexTime;
    }

    public Date getSyncBeginTime() {
        return syncBeginTime;
    }

    public void setSyncBeginTime(Date syncBeginTime) {
        this.syncBeginTime = syncBeginTime;
    }

    public Date getSyncEndTime() {
        return syncEndTime;
    }

    public void setSyncEndTime(Date syncEndTime) {
        this.syncEndTime = syncEndTime;
    }
}
