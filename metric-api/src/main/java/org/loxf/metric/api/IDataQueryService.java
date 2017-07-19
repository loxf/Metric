package org.loxf.metric.api;

import org.loxf.metric.common.dto.ChartData;
import org.loxf.metric.common.dto.ConditionVo;

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
}
