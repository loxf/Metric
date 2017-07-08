//package org.loxf.metric.client;
//
//
//import org.loxf.metric.common.dto.BaseResult;
//import org.loxf.metric.common.dto.ChartDto;
//import org.loxf.metric.common.dto.PageData;
//
//import java.util.Map;
//
///**
// * Created by caiyang on 2017/5/4.
// */
//public interface ChartService extends  IBaseService<ChartDto>{
//    /**
//     * 获取图列表支持分页
//     * @param chartDto
//     * @return
//     */
//    public PageData listChartPage(ChartDto chartDto);
//
//
//    /**
//     * 创建图
//     * @param chartDto
//     * @return
//     */
//    public BaseResult<String> createChart(ChartDto chartDto);
//
//    /**
//     * 获取图
//     * @param chartCode
//     * @return
//     */
//    public BaseResult<ChartDto> queryChartByCode(String chartCode);
//
//
//    public BaseResult<String> updateChart(String chartCode,Map<String, Object> setParams);
//
//    /**
//     * 删除图
//     * @param chartCode
//     * @return
//     */
//    public BaseResult<String> delChartByCode(String chartCode);
//}
