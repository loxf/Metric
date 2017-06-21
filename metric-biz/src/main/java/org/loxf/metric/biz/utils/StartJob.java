package org.loxf.metric.biz.utils;

import org.loxf.metric.core.quartz.JobEntity;
import org.loxf.metric.core.quartz.SchedulerHelper;
import org.loxf.metric.core.quartz.StartJobSchedulerListener;
import org.loxf.metric.dal.po.SyncJob;
import org.loxf.metric.service.SyncJobManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luohj on 2017/5/4.
 */
public class StartJob {
    private static Logger logger = LoggerFactory.getLogger(StartJob.class);
    @Autowired
    private SchedulerHelper schedulerHelper;
    @Autowired
    private SyncJobManager jobManager;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    public void init(){
       String enable= PropertiesUtil.getValue("job.enable");
       if(StringUtils.isNotBlank(enable) && "1".equals(enable)) {
           try {
               SchedulerListener[] listeners = {new StartJobSchedulerListener()};
               schedulerHelper.init(listeners, schedulerFactoryBean.getScheduler());
           } catch (Exception e) {
               logger.error("初始化QUARTZ-JOB失败", e);
           }
           try {
               List<JobEntity> jobs = new ArrayList<>();
               List<SyncJob> jobList = jobManager.getNotInitedJobs();
               if (CollectionUtils.isNotEmpty(jobList)) {
                   formatterJobs(jobs, jobList);
               }
               // add job
               schedulerHelper.createActiveJobFromDB(jobs);
               // inited JOB
               for (SyncJob job : jobList) {
                   jobManager.initJob(job);
               }
           } catch (SchedulerException e) {
               logger.error("初始化任务失败：", e);
           }
       } else {
           try {
               schedulerFactoryBean.getScheduler().shutdown();
           } catch (SchedulerException e) {
               logger.error("QUARTZ-JOB关闭失败", e);
           }
       }
    }
    private void formatterJobs(List<JobEntity> target, List<SyncJob> source){
        for(SyncJob job : source){
            JobEntity tmp = new JobEntity();
            BeanUtils.copyProperties(job, tmp);
            tmp.setJobId(job.getJobCode());
            target.add(tmp);
        }
    }
}
