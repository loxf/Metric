package org.loxf.metric.biz.task;

import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.biz.base.SpringApplicationContextUtil;
import org.loxf.metric.core.quartz.Task;
import org.loxf.metric.dal.po.SyncJob;
import org.loxf.metric.dal.po.SyncJobInst;
import org.loxf.metric.service.SyncJobInstManager;
import org.loxf.metric.service.SyncJobManager;
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
public class ExcelSyncTask extends Task {
    private SyncJobManager syncJobManager;
    private SyncJobInstManager syncJobInstManager;

    private SyncJob job;
    private List<Long> newCircleTimes = new ArrayList<Long>();
    private long newLastCircleTime ;// 最新的下一账期
    private long interval;
    private int total;

    public ExcelSyncTask() {
        syncJobManager = (SyncJobManager) SpringApplicationContextUtil.getBean(SyncJobManager.class);
        syncJobInstManager = (SyncJobInstManager) SpringApplicationContextUtil.getBean(SyncJobInstManager.class);
    }

    public boolean init(JobExecutionContext context){
        job = syncJobManager.getJob(jobEntity.getJobId());
        return true;
    }

    public boolean recoveryJob(JobExecutionContext context){
        return true;
    }

    @Override
    public boolean preExcute(JobExecutionContext context) {
        // 无论是否有数据执行，都要更新
        updateSyncJob(0, beginTime, 1);
        // 计算账期
        interval = job.getParticleSize();// 粒度单位是分，或者 周月年
        // 获取上一个账期 如果没有，则获取第一次运行账期（不能为空），则当前时间-粒度*2
        long lastCircleTime = getLastCircleTime(interval, job.getLastCircleTime(), job.getFirstRunTime());
        if(lastCircleTime==0){
            throw new MetricException(job.getJobName() + job.getJobCode() + " - First_Run_Time不能为空");
        }
        List<Long> tmp = new ArrayList<Long>();
        newLastCircleTime = lastCircleTime;//初始化
        long nextCircleTime = getNextCircleTime(new Date(lastCircleTime), interval);
        while (true) {
            if (getNextCircleTime(new Date(nextCircleTime), interval) < System.currentTimeMillis()) {
                // 新账期的结束时间小于当前时间
                tmp.add(nextCircleTime);
                nextCircleTime = getNextCircleTime(new Date(nextCircleTime), interval);
            } else {
                break;// 新账期的结束时间大于当前时间 跳出循环
            }
        }
        if (tmp.size() > 0) {
            // 存在可以执行的账期 创建账期数据
            createSyncInstJob(tmp);
        }
        // 获取库中状态为待处理的账期实例
        List<SyncJobInst> jobInstList = syncJobInstManager.getWaitingInst(job.getJobCode());
        if(jobInstList!=null && jobInstList.size()>0){
            for(SyncJobInst syncJobInst: jobInstList) {
                newCircleTimes.add(syncJobInst.getCircleTime().getTime());
            }
        }
        if(newCircleTimes.size() == 0) {
            logger.debug("当前任务[" + job.getJobName() + ":" + job.getJobCode() + "]无可用账期执行。", "BUSI0001");
            return false;
        }
        return true;
    }

    @Override
    public void exeucuteInternal(JobExecutionContext context) {
    }

    @Override
    public void afterExcute(JobExecutionContext context) {
    }

    @Override
    public void excptionDeal(JobExecutionContext context) {
    }

    @Override
    public void finallyDeal(JobExecutionContext context) {
        updateSyncJob(newLastCircleTime, beginTime, 0);
    }

    /**
     * @param lastCircleTime
     * @param beginTime
     * @param state          WAITING:0 , RUNNING:1
     */
    public void updateSyncJob(long lastCircleTime, long beginTime, int state) {
        if (lastCircleTime > 0)
            job.setLastCircleTime(new Date(lastCircleTime));
        job.setRunLastTime(new Date(beginTime));
        job.setStatus(state);
        syncJobManager.updateStatus(job);
    }

    private void createSyncInstJob(List<Long> circleList) {
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

}
