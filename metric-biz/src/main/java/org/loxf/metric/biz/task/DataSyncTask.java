package org.loxf.metric.biz.task;

import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.biz.base.SpringApplicationContextUtil;
import org.loxf.metric.biz.utils.CircleTimeUtil;
import org.loxf.metric.dal.po.SyncJob;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by luohj on 2017/5/4.
 */
public class DataSyncTask extends SyncTask {
    private static Logger logger = LoggerFactory.getLogger(DataSyncTask.class);
    private ITask task;

    /**
     * 数据同步任务
     */
    public DataSyncTask(){}

    /**
     * @param job
     * @return
     */
    @Override
    public boolean init(SyncJob job) {
        try {
            task = (ITask) SpringApplicationContextUtil.getBean(job.getDataOperClass());
            return true;
        } catch (Exception e) {
            logger.error("任务初始化失败[" + job.getJobName() + "]" + "。请检查DATA_OPER_CLASS配置是否正确！", e);
            throw new MetricException("任务初始化失败[" + job.getJobName() + "]" + "。请检查DATA_OPER_CLASS配置是否正确！", e);
        }
    }

    /**
     * @param job
     * @param circleTime
     * @return 返回同步的数据条数
     */
    @Override
    public int doTask(SyncJob job, Long circleTime){
        int syncNbr = 0, currentPage = 0, row = 1000;
        task.init(job, circleTime);
        while (true) {
            // 取数据 每次取有限的数据处理
            List data = task.queryData(job, circleTime, currentPage, row);
            if (CollectionUtils.isEmpty(data)) {
                break;// 当前账期处理完成
            } else {
                // 入库
                try {
                    // 下面这个方法是事务的
                    task.writeDB(job, circleTime, data);
                    // 维护列数据
                    task.updateDim(job, data);
                    syncNbr += data.size();
                    currentPage++;
                } catch (Exception e){
                    logger.error("入库失败[" + job.getJobName() + "]-账期：" + CircleTimeUtil.formatCircleTime(circleTime), e);
                    throw new MetricException("入库失败[" + job.getJobName() + "]-账期：" + CircleTimeUtil.formatCircleTime(circleTime) + ":" + e.getMessage(), e);
                }
            }
        }
        return syncNbr;
    }

    /**
     * @param job
     * @param circleTime
     * @return
     */
    @Override
    public int getSyncTotalNbr(SyncJob job, Long circleTime) {
        return task.queryTotal(job, circleTime);
    }
}
