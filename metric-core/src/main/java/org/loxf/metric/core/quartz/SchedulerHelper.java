package org.loxf.metric.core.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;

/**
 * Created by luohj on 2017/5/4.
 */
public class SchedulerHelper {
    private static Logger logger = LoggerFactory.getLogger(SchedulerHelper.class);
    private static final String CONFIG_FILE = "quartz/quartz-job.properties";
    private static final String IDENTITY_JOB_PREFIX = "job_";
    private static final String IDENTITY_TRIGGER_PREFIX = "trigger_";
    private Scheduler scheduler;


    /**
     * tomcat一启动时，类实例化时就执行
     */
    public void init(SchedulerListener[] listeners, Scheduler _scheduler) {
        try {
            // 创建一个定时器工厂
            /*StdSchedulerFactory sf = new StdSchedulerFactory();
            //初始化quartz-job.properties配置文件
            sf.initialize(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE));
            scheduler = sf.getScheduler();*/
            //把jobService放到scheduler上下文，job执行时可以获取并访问。
            // scheduler.getContext().put(SCHEDULER_KEY_JOBSERVICE, jobService);
            //设置自己的监听器

            // startJobSchedulerListener.setSchedulerHelper(this);
            this.scheduler = _scheduler;
            if (listeners!=null && listeners.length>0) {
                for(SchedulerListener listener : listeners) {
                    scheduler.getListenerManager().addSchedulerListener(listener);
                }
            }
            // 启动定时器
            if(!scheduler.isShutdown()) {
                scheduler.start();
            }
            logger.info("====================job scheduler start");
        } catch (SchedulerException e) {
            logger.error("error", e);
        }
    }


    /**
     * 根据JobEntity创建并开始任务
     */
    public boolean createAndStartJob(JobEntity job) {
        try {
            JobDetail jobDetail = generateJobDetail(job);
            Trigger trigger = generateTriggerBuilder(job).build();
            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (SchedulerException e) {
            logger.error("scheduler.scheduleJob", e);
            return false;
        } catch (ClassNotFoundException e) {
            logger.error("scheduler.scheduleJob", e);
            return false;
        }
    }

    /**
     * 清除
     */
    public void clearAllScheduler() {
        try {
            scheduler.clear();
        } catch (SchedulerException e) {
            logger.error("clearAllScheduler", e);
        }
    }


    /**
     * 根据jobId和类型删除
     */
    public boolean removeJob(String jobId, String jobType) {
        try {
            TriggerKey triggerKey = getTriggerKey(jobId, jobType);
            scheduler.pauseTrigger(triggerKey);
            if(scheduler.unscheduleJob(triggerKey) && scheduler.deleteJob(getJobKey(jobId, jobType))) {
                return true;
            } else {
                return false;
            }
        } catch (SchedulerException e) {
            logger.error("removeJob", e);
            return false;
        }
    }

    /**
     * 暂停任务
     */
    public boolean pauseJob(String jobId, String jobType) {
        try {
            scheduler.pauseJob(getJobKey(jobId, jobType));
            return true;
        } catch (SchedulerException e) {
            logger.error("resumeJob", e);
            return false;
        }
    }

    public boolean modifyJobTime(JobEntity newJob){
        CronTrigger trigger = null;
        try {
            trigger = (CronTrigger) scheduler.getTrigger(getTriggerKey(newJob.getJobId(), newJob.getJobType()));
            if (trigger == null) {
                return false;
            }
            String oldCron = trigger.getCronExpression();
            if(!oldCron.equalsIgnoreCase(newJob.getCron())){
                if(removeJob(newJob.getJobId(), newJob.getJobType())){
                    return createAndStartJob(newJob);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (SchedulerException e) {
            logger.error("modifyJobTime", e);
            return false;
        }
    }

    /**
     * 马上只执行一次任务
     */
    public boolean executeOnceJob(JobEntity job) {
        try {
            Calendar end = Calendar.getInstance();
            TriggerBuilder<SimpleTrigger> simpleTriggerBuilder = TriggerBuilder.newTrigger()
                    .withIdentity(getTriggerKey(job.getJobId(), job.getJobType()))
                    .forJob(getJobKey(job.getJobId(), job.getJobType()))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2));
            end.add(Calendar.SECOND, 2);
            simpleTriggerBuilder.startAt(end.getTime());
            end.add(Calendar.SECOND, 5);
            simpleTriggerBuilder.endAt(end.getTime());

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("jobEntity", job);
            simpleTriggerBuilder.usingJobData(jobDataMap);
            Trigger trigger = simpleTriggerBuilder.build();

            scheduler.scheduleJob(trigger);
            return true;
        } catch (SchedulerException e) {
            logger.error("executeOneceJob", e);
            return false;
        }
    }

    /**
     * 启动一些scheduler里没有的active的jobDetail
     */
    public void createActiveJobFromDB(List<JobEntity> jobs) throws SchedulerException {
        for (JobEntity job : jobs) {
            if (scheduler.getJobDetail(getJobKey(job)) == null)
                createAndStartJob(job);
        }
    }

    /**
     * 获得任务的jobKey
     */
    public static JobKey getJobKey(String jobId, String jobType) {
        return new JobKey(IDENTITY_JOB_PREFIX + jobId, IDENTITY_JOB_PREFIX + jobType);
    }

    /**
     * 获得任务的jobKey
     */

    public static JobKey getJobKey(JobEntity job) {
        return new JobKey(IDENTITY_JOB_PREFIX + job.getJobId(), IDENTITY_JOB_PREFIX + job.getJobType());
    }

    /**
     * 获得trigger的triggerkey
     */
    public static TriggerKey getTriggerKey(JobEntity job) {
        return new TriggerKey(IDENTITY_TRIGGER_PREFIX + job.getJobId(), IDENTITY_TRIGGER_PREFIX + job.getJobType());
    }


    /**
     * 获得trigger的triggerkey
     */
    public static TriggerKey getTriggerKey(String jobId, String jobType) {
        return new TriggerKey(IDENTITY_TRIGGER_PREFIX + jobId, IDENTITY_TRIGGER_PREFIX + jobType);
    }

    public static JobDetail generateJobDetail(JobEntity job) throws ClassNotFoundException {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobEntity", job);
        Class clazz = Class.forName(job.getClazz());
        return JobBuilder.newJob(clazz)
                .withIdentity(getJobKey(job))
                .usingJobData(jobDataMap)
                .requestRecovery(true).storeDurably(true)
                .build();
    }


    /**
     * 根据jobEntity获得trigger
     */

    public static TriggerBuilder<CronTrigger> generateTriggerBuilder(JobEntity job) {
        TriggerBuilder<CronTrigger> triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(job))
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron())
                        .withMisfireHandlingInstructionDoNothing());
        if (job.getSyncBeginTime() != null)
            triggerBuilder.startAt(job.getSyncBeginTime());
        else
            triggerBuilder.startNow();

        if (job.getSyncEndTime() != null)
            triggerBuilder.endAt(job.getSyncEndTime());

        return triggerBuilder;
    }

}