package org.loxf.metric.core.quartz;

import org.loxf.metric.base.exception.MetricException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by luohj on 2017/5/4.
 */
public abstract class Task implements Job {
    protected JobEntity jobEntity;
    protected static final Logger logger= LoggerFactory.getLogger(Task.class);
    protected Long beginTime;
    protected Long endTime;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        try {
            beginTime = System.currentTimeMillis();
            jobEntity = (JobEntity) context.getJobDetail().getJobDataMap().get("jobEntity");
            // 初始化JOB
            if(init(context)) {
                // 如果是中断恢复执行，需要执行清理逻辑
                if(context.isRecovering()){
                    // 获取job对应的在处理中的账期
                    recoveryJob(context);
                    logger.debug("JOB恢复执行的数据清理已经完成!");
                }
                // 正式处理
                // 预处理 生成账期数据
                if (preExcute(context)) {
                    // 任务执行
                    exeucuteInternal(context);
                    // 执行后处理
                    afterExcute(context);
                }
            } else {
                logger.error("初始化时失败!");
            }
        } catch (Exception e){
            logger.error("执行异常：", e);
            // 异常处理
            excptionDeal(context);
        } finally {
            endTime=System.currentTimeMillis();
            // 一定会处理
            finallyDeal(context);
        }
    }

    public abstract boolean init(JobExecutionContext context);

    public abstract boolean recoveryJob(JobExecutionContext context);

    public abstract void exeucuteInternal(JobExecutionContext context);

    public abstract boolean preExcute(JobExecutionContext context);

    public abstract void afterExcute(JobExecutionContext context);

    public abstract void excptionDeal(JobExecutionContext context);

    public abstract void finallyDeal(JobExecutionContext context);

    public void setJobEntity(JobEntity jobEntity) {
        this.jobEntity = jobEntity;
    }

    /**
     * @param interval 间隔 >0为分钟，-1为周 -2为月 -3 为年
     * @param lastTime 上一账期
     * @param firstRunTime 第一个账期
     * @return
     */
    public long getLastCircleTime(long interval, Date lastTime, Date firstRunTime){
        long lastCircleTime = 0;
        if(interval>0) {
            // 按分钟数计算
            lastCircleTime = lastTime == null ? (firstRunTime == null ? 0 : firstRunTime.getTime() - interval*60*1000)
                    : lastTime.getTime();
        } else if(interval==-1){
            //按周
            lastCircleTime = lastTime == null ? (firstRunTime == null ? 0 : firstRunTime.getTime() - 7*interval*60*1000)
                    : lastTime.getTime();
        } else if(interval==-2){
            //按月
            Date tmp = lastTime;
            if (tmp == null) {
                tmp = firstRunTime;
                if(tmp!=null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(tmp);
                    c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
                    lastCircleTime = c.getTime().getTime();
                }
            } else {
                lastCircleTime = tmp.getTime();
            }
        } else if(interval==-3){
            //按年
            Date tmp = lastTime;
            if (tmp == null) {
                tmp = firstRunTime;
                if(tmp!=null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(tmp);
                    c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
                    lastCircleTime = c.getTime().getTime();
                }
            } else {
                lastCircleTime = tmp.getTime();
            }
        } else {
            throw new MetricException("账期间隔暂不支持当前参数：" + interval);
        }
        return lastCircleTime;
    }

    /**
     * @param currentTime 当前账期
     * @param interval 间隔 >0为分钟，-1为周 -2为月 -3 为年
     * @return
     */
    public long getNextCircleTime(Date currentTime, long interval){
        long nextTime = 0;
        if(interval>0) {
            // 按分钟数计算
            nextTime = currentTime.getTime() + interval*60*1000;
        } else if(interval==-1){
            //按周
            nextTime = currentTime.getTime() + 7*interval*60*1000;
        } else if(interval==-2){
            //按月
            Calendar c = Calendar.getInstance();
            c.setTime(currentTime);
            c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
            nextTime = c.getTime().getTime();
        } else if(interval==-3){
            //按年
            Calendar c = Calendar.getInstance();
            c.setTime(currentTime);
            c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
            nextTime = c.getTime().getTime();
        } else {
            throw new MetricException("账期间隔暂不支持当前参数：" + interval);
        }
        return nextTime;
    }
}
