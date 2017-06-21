package org.loxf.metric.dal.dao;

import org.apache.ibatis.annotations.Param;
import org.loxf.metric.dal.po.SyncJob;

import java.util.Date;
import java.util.List;

public interface SyncJobMapper {
    List<SyncJob> selectNotInitedJobs();

    int initJob(@Param("jobCode") String jobCode, @Param("lastCircleTime") Date lastCircleTime);

    int updateJobStatus(SyncJob job);

    SyncJob getJob(String jobCode);

    List<SyncJob> getAllJob(SyncJob job);

    int getAllJobCount(SyncJob job);

    int recoverySyncJob(@Param("jobCode") String jobCode, @Param("lastCircleTime") Date lastCircleTime);

    int updateCron(@Param("jobCode") String jobCode, @Param("cron") String cron);
}