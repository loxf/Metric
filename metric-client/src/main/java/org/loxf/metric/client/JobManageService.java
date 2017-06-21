package org.loxf.metric.client;


import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.SyncJobDto;
import org.loxf.metric.common.dto.SyncJobInstDto;

import java.util.Date;

/**
 * Created by luohj on 2017/5/27.
 */
public interface JobManageService {
    /**
     * 获取所有JOB
     * @return
     */
    public PageData<SyncJobDto> queryAllSyncJob(SyncJobDto jobDto);
    /**
     * 获取JOB实例
     * @param dto
     * @return
     */
    public PageData<SyncJobInstDto> querySyncJobInst(SyncJobInstDto dto);

    /**
     * 重启某个JOB的某个账期
     * @param jobCode
     * @param circleTime
     */
    public void recoveryJobByCircleTime(String jobCode, Date circleTime);

    /**
     * 重新设置JOB的运行时间
     * @param jobCode
     * @param cron
     */
    public BaseResult settingJobCron(String jobCode, String cron);

    /**
     * 重启某个JOB的全部账期
     * @param jobCode
     */
    public BaseResult recoveryAllJob(String jobCode);
}
