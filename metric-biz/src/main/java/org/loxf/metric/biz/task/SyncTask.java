package org.loxf.metric.biz.task;

import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.biz.base.SpringApplicationContextUtil;
import org.loxf.metric.common.constants.JobState;
import org.loxf.metric.core.quartz.Task;
import org.loxf.metric.dal.po.SyncJob;
import org.loxf.metric.dal.po.SyncJobInst;
import org.loxf.metric.service.SyncJobInstManager;
import org.loxf.metric.service.SyncJobManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luohj on 2017/5/5.
 */
@DisallowConcurrentExecution
public abstract class SyncTask extends Task {
    private SyncJobManager syncJobManager;
    private SyncJobInstManager syncJobInstManager;

    private SyncJob job;
    private List<Long> newCircleTimes = new ArrayList<>();
    private long newLastCircleTime ;// 最新的下一账期
    private long interval;

    /**
     * 同步任务
     */
    public SyncTask() {
        syncJobManager = (SyncJobManager) SpringApplicationContextUtil.getBean(SyncJobManager.class);
        syncJobInstManager = (SyncJobInstManager) SpringApplicationContextUtil.getBean(SyncJobInstManager.class);
    }

    /**
     * @param context
     * @return
     */
    @Override
    public boolean init(JobExecutionContext context){
        job = syncJobManager.getJob(jobEntity.getJobId());
        return init(job);
    }

    /**
     * @param context
     * @return
     */
    @Override
    public boolean recoveryJob(JobExecutionContext context){
        String dataTable = StringUtils.isEmpty(job.getDataTable())?"quota_data_" + job.getJobCode() : job.getDataTable();
        return cleanRecoveryJobData(job.getJobCode(), job.getDataTable());
    }

    /**
     * 具体的任务执行获取当前账期的总数不一样，自行修改
     * @param job
     * @return
     */
    public abstract boolean init(SyncJob job);
    /**
     * 具体的任务执行获取当前账期的总数不一样，自行修改
     * @param job
     * @param newCircleTime
     * @return
     */
    public abstract int doTask(SyncJob job, Long newCircleTime);

    /**
     * @param context
     * @return
     */
    @Override
    public boolean preExcute(JobExecutionContext context) {
        // 无论是否有数据执行，都要更新
        updateSyncJob(0, beginTime, 1);
        // 计算账期
        interval = job.getParticleSize() ;// 粒度单位是分，或者 周月年
        // 获取上一个账期 如果没有，则获取第一次运行账期（不能为空），则当前时间-粒度*2
        long lastCircleTime = getLastCircleTime(interval, job.getLastCircleTime(), job.getFirstRunTime());
        if(lastCircleTime==0){
            throw new MetricException(job.getJobName() + job.getJobCode() + " - First_Run_Time不能为空");
        }
        List<Long> tmp = new ArrayList<>();
        newLastCircleTime = lastCircleTime;//初始化
        long nextCircleTime = getNextCircleTime(new Date(lastCircleTime), interval);
        while (true) {
            if (getNextCircleTime(new Date(nextCircleTime), interval) < System.currentTimeMillis()) {
                // 新账期的结束时间小于当前时间
                tmp.add(nextCircleTime);
                nextCircleTime = getNextCircleTime(new Date(nextCircleTime), interval) ;
            } else {
                break;// 新账期的结束时间大于当前时间 跳出循环
            }
        }
        if (CollectionUtils.isNotEmpty(tmp)) {
            // 存在可以执行的账期 创建账期数据
            createSyncInstJob(tmp);
        }
        // 获取库中状态为待处理的账期实例
        List<SyncJobInst> jobInstList = syncJobInstManager.getWaitingInst(job.getJobCode());
        if(CollectionUtils.isNotEmpty(jobInstList)){
            for(SyncJobInst syncJobInst: jobInstList) {
                newCircleTimes.add(syncJobInst.getCircleTime().getTime());
            }
        }
        if(CollectionUtils.isEmpty(newCircleTimes)) {
            logger.debug("当前任务[" + job.getJobName() + ":" + job.getJobCode() + "]无可用账期执行。", "BUSI0001");
            return false;
        }
        return true;
    }

    /**
     * @param context
     */
    @Override
    public void exeucuteInternal(JobExecutionContext context) {
        // 执行任务
        for (Long newCircleTime : newCircleTimes) {
            updateSyncInstJob(newCircleTime, JobState.RUNNING.getValue(), JobState.RUNNING.getDesc(), 0);
            int syncNbr = 0;
            try {
                syncNbr = doTask(job, newCircleTime);
                updateSyncInstJob(newCircleTime, JobState.COMPLETED.getValue(), JobState.COMPLETED.getDesc(), syncNbr);
            } catch (Exception e) {
                updateSyncInstJob(newCircleTime, JobState.COMPLETED_FAILED.getValue(),
                        JobState.COMPLETED_FAILED.getDesc() + ":" + e.getMessage(), syncNbr);
                throw e;
            }
        }
    }

    /**
     * @param context
     */
    @Override
    public void afterExcute(JobExecutionContext context) {

    }

    /**
     * @param context
     */
    @Override
    public void excptionDeal(JobExecutionContext context) {
    }

    /**
     * @param context
     */
    @Override
    public void finallyDeal(JobExecutionContext context) {
        updateSyncJob(newLastCircleTime, beginTime, 0);
    }

    /**
     * @param lastCircleTime
     * @param beginTime
     * @param state          WAITING:0 , RUNNING:1
     */
    void updateSyncJob(long lastCircleTime, long beginTime, int state) {
        if (lastCircleTime > 0)
            job.setLastCircleTime(new Date(lastCircleTime));
        job.setRunLastTime(new Date(beginTime));
        job.setStatus(state);
        syncJobManager.updateStatus(job);
    }

    void updateSyncInstJob(long newCircleTime, int state, String remark, int syncNbr) {
        SyncJobInst inst = new SyncJobInst();
        BeanUtils.copyProperties(job, inst);
        inst.setCircleTime(new Date(newCircleTime));
        inst.setState(state);
        if(remark.length()>500){
            remark = remark.substring(0,500);
        }
        inst.setRemark(remark);
        inst.setSyncNbr(syncNbr);
        inst.setTotal(getSyncTotalNbr(job, newCircleTime) );
        if(state==JobState.COMPLETED.getValue()||state==JobState.COMPLETED_FAILED.getValue())
            inst.setCompleteDate(new Date(System.currentTimeMillis()));
        syncJobInstManager.updateSyncInst(inst);
    }

    void createSyncInstJob(List<Long> circleList) {
        for (Long newCircleTime : circleList) {
            if(newCircleTime>newLastCircleTime){
                newLastCircleTime = newCircleTime;
            }
            if (syncJobInstManager.existsSyncInst(job.getJobCode(), new Date(newCircleTime)) == 0) {
                SyncJobInst inst = new SyncJobInst();
                BeanUtils.copyProperties(job, inst);
                inst.setCircleTime(new Date(newCircleTime));
                syncJobInstManager.createSyncInst(inst);
            }
        }
    }

    boolean cleanRecoveryJobData(String jobCode, String dataTable){
        try {
            List<SyncJobInst> needRecoveryJobInstList = syncJobInstManager.getExecutingInst(jobCode);
            if(CollectionUtils.isNotEmpty(needRecoveryJobInstList)){
                for(SyncJobInst syncJobInst : needRecoveryJobInstList){
                    // 清理数据
                    syncJobInstManager.cleanDataByCircleTime(syncJobInst.getCircleTime(), dataTable);
                    // 回复JOBIINST状态
                    syncJobInstManager.recoverySyncJobInst(syncJobInst.getJobCode(), syncJobInst.getCircleTime());
                }

            }
        } catch (Exception e){
            throw new MetricException("数据恢复执行错误", e);
        }
        return true;
    }

    /**
     * @param job
     * @param circleTime
     * @return
     */
    public int getSyncTotalNbr(SyncJob job, Long circleTime) {
        return 0;
    }
}
