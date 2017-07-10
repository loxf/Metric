package org.loxf.metric.service.task;

import org.loxf.metric.dal.po.SyncJob;

import java.util.List;

/**
 * 事务控制在这一级
 * Created by luohj on 2017/5/5.
 */
public interface ITask {
    // String[] tableNamesFixed = {"id","source_id","value","busi_domain","circle_time","created_at"};
    /**
     * init
     * @param job
     * @param circleTime
     */
    default public void init(SyncJob job, long circleTime){}

    /**
     * @param job JOB信息
     * @param circleTime 账期
     * @param currentPage 数据分页 当前页
     * @param row 数据分页 每页数
     * @return
     */
    public List queryData(SyncJob job, long circleTime, int currentPage, int row);

    /**
     * 获取当前账期同步总数
     * @param job
     * @param circleTime
     * @return
     */
    default public int queryTotal(SyncJob job, long circleTime){
        return 0;
    }

    /**
     * @param job
     * @param circleTime
     * @param data
     */
    public void writeDB(SyncJob job, long circleTime, List data);

    /**
     * 维护列数据
     * @param data
     */
    default public void updateDim(SyncJob job, List data){}
}
