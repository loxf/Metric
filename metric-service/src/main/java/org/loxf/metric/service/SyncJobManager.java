package org.loxf.metric.service;

import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.SyncJobDto;
import org.loxf.metric.dal.dao.SyncJobMapper;
import org.loxf.metric.dal.po.SyncJob;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by luohj on 2017/5/5.
 */
@Component
public class SyncJobManager {
    @Autowired
    private SyncJobMapper jobMapper;

    public PageData<SyncJobDto> getAllJob(SyncJobDto job){
        SyncJob syncJob = new SyncJob();
        BeanUtils.copyProperties(job, syncJob);
        int count = jobMapper.getAllJobCount(syncJob);
        int totalPage = count/job.getRow() + (count%job.getRow()==0?0:1);
        List<SyncJob> list = jobMapper.getAllJob(syncJob);
        List<SyncJobDto> ret = new ArrayList<>();
        for(SyncJob tmp : list){
            SyncJobDto dto = new SyncJobDto();
            BeanUtils.copyProperties(tmp, dto);
            ret.add(dto);
        }
        return new PageData<SyncJobDto>(totalPage, count, ret);
    }

    public SyncJob getJob(String jobCode){
        return jobMapper.getJob(jobCode);
    }

    public List<SyncJob> getNotInitedJobs(){
        return jobMapper.selectNotInitedJobs();
    }

    public int updateStatus(SyncJob job){
        return jobMapper.updateJobStatus(job);
    }

    public int initJob(SyncJob job){
        return jobMapper.initJob(job.getJobCode(), new Date(getLastCircleTime(job.getFirstRunTime(), job.getParticleSize())));
    }

    public int recoverySyncJob(String jobCode){
        SyncJob job = getJob(jobCode);
        return jobMapper.recoverySyncJob(jobCode, new Date(getLastCircleTime(job.getFirstRunTime(), job.getParticleSize())));
    }
    public int updateCron(String jobCode, String cron){
        return jobMapper.updateCron(jobCode, cron);
    }

    /**
     * @param currentTime 当前账期
     * @param interval 间隔 >0为分钟，-1为周 -2为月 -3 为年
     * @return
     */
    public long getLastCircleTime(Date currentTime, long interval){
        long nextTime = 0;
        if(interval>0) {
            // 按分钟数计算
            nextTime = currentTime.getTime() - interval*60*1000;
        } else if(interval==-1){
            //按周
            nextTime = currentTime.getTime() - 7*interval*60*1000;
        } else if(interval==-2){
            //按月
            Calendar c = Calendar.getInstance();
            c.setTime(currentTime);
            c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
            nextTime = c.getTime().getTime();
        } else if(interval==-3){
            //按年
            Calendar c = Calendar.getInstance();
            c.setTime(currentTime);
            c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
            nextTime = c.getTime().getTime();
        } else {
            throw new MetricException("账期间隔暂不支持当前参数：" + interval);
        }
        return nextTime;
    }
}
