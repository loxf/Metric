package org.loxf.metric.biz.impl;

import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.client.JobManageService;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.SyncJobDto;
import org.loxf.metric.common.dto.SyncJobInstDto;
import org.loxf.metric.core.quartz.JobEntity;
import org.loxf.metric.core.quartz.SchedulerHelper;
import org.loxf.metric.dal.po.SyncJob;
import org.loxf.metric.service.SyncJobInstManager;
import org.loxf.metric.service.SyncJobManager;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by luohj on 2017/5/27.
 */
@Service("jobManageService")
public class JobManagerServiceImpl implements JobManageService {
    private static Logger logger = LoggerFactory.getLogger(JobManagerServiceImpl.class);
    @Autowired
    private SchedulerHelper schedulerHelper;
    @Autowired
    private SyncJobManager syncJobManager;
    @Autowired
    private SyncJobInstManager syncJobInstManager;

    @Override
    public PageData<SyncJobDto> queryAllSyncJob(SyncJobDto jobDto){
        try {
            PageData<SyncJobDto> list = syncJobManager.getAllJob(jobDto);
            return list;
        } catch (Exception e){
            logger.error("获取JOB失败：" + e.getMessage(), e);
            throw new MetricException("获取JOB失败：" + e.getMessage(), e);
        }
    }

    @Override
    public PageData<SyncJobInstDto> querySyncJobInst(SyncJobInstDto dto) {
        return syncJobInstManager.getSyncJobInst(dto);
    }

    @Override
    @Transactional
    public void recoveryJobByCircleTime(String jobCode, Date circleTime) {
        try {
            SyncJob syncJob = syncJobManager.getJob(jobCode);
            String dataTable = StringUtils.isEmpty(syncJob.getDataTable()) ? "quota_data_" + jobCode : syncJob.getDataTable();
            syncJobInstManager.recoverySyncJobInst(jobCode, circleTime);
            syncJobInstManager.cleanDataByCircleTime(circleTime, dataTable);
        } catch (Exception e){
            logger.error("重启JOB-" + jobCode + ", 账期：" + circleTime.toString() + "失败：" + e.getMessage(), e);
            throw new MetricException("重启JOB-" + jobCode + ", 账期：" + circleTime.toString() + "失败：" + e.getMessage(), e);
        }
    }

    @Override
    public BaseResult settingJobCron(String jobCode, String cron) {
        SyncJob syncJob = syncJobManager.getJob(jobCode);
        if(CronExpression.isValidExpression(cron)) {
            if (!cron.equalsIgnoreCase(syncJob.getCron())) {
                syncJobManager.updateCron(jobCode, cron);
                JobEntity jobEntity = formatterJobs(syncJob);
                jobEntity.setCron(cron);
                if (schedulerHelper.modifyJobTime(jobEntity))
                    return new BaseResult();
                else
                    return new BaseResult(0, "重置失败，检查后台日志");
            } else {
                return new BaseResult(0, "当前CRON表达式未发生改变");
            }
        } else {
            return new BaseResult(0, "CRON表达式错误");
        }
    }

    @Override
    @Transactional
    public BaseResult recoveryAllJob(String jobCode) {
        try {
            SyncJob syncJob = syncJobManager.getJob(jobCode);
            String dataTable = StringUtils.isEmpty(syncJob.getDataTable())?"quota_data_" + jobCode:syncJob.getDataTable();
            // 删除所有的账期实例
            syncJobInstManager.deleteAllInstByJobCode(jobCode);
            // 更新JOB的上一账期时间为第一次运行时间的前一天
            syncJobManager.recoverySyncJob(jobCode);
            syncJobInstManager.cleanDataTable(dataTable);
            return new BaseResult();
        } catch (Exception e){
            logger.error("重启JOB-" + jobCode + "失败：" + e.getMessage(), e);
            throw new MetricException("重启JOB-" + jobCode + "失败：" + e.getMessage(), e);
        }
    }
    private JobEntity formatterJobs(SyncJob source){
        JobEntity result = new JobEntity();
        BeanUtils.copyProperties(source, result);
        result.setJobId(source.getJobCode());
        return result;
    }
}
