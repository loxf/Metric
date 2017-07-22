package org.loxf.metric.api;

import org.loxf.metric.common.dto.*;

import java.util.List;

/**
 * Created by luohj on 2017/7/15.
 */
public interface IDataQueryService {

    /**
     * 获取首页数据
     * @param handleUserName
     * @param uniqueCode
     * @param condition
     * @return
     */
    public List<ChartData> getIndexData(String handleUserName, String uniqueCode, ConditionVo condition);
    /**
     * 获取看板数据
     * @param handleUserName
     * @param boardCode
     * @param condition
     * @return
     */
    public List<ChartData> getBoardData(String handleUserName, String boardCode, ConditionVo condition);
    /**
     * 获取图数据
     * @param handleUserName
     * @param chartCode
     * @param condition
     * @return
     */
    public ChartData getChartData(String handleUserName, String chartCode, ConditionVo condition);

    /**
     * 获取目标数据
     * @param dto
     * @return
     */
    public TargetData getTargetData(TargetDto dto);

    /**
     * 获取指标的值
     * @param quotaCode
     * @param summaryOperation
     * @param condition
     * @return
     */
    public BaseResult getQuotaData(String quotaCode, String summaryOperation, ConditionVo condition);
}
