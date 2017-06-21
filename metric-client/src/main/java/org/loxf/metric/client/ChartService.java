package org.loxf.metric.client;


import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.common.dto.PageData;

/**
 * Created by caiyang on 2017/5/4.
 */
public interface ChartService {
    /**
     * 获取图列表支持分页
     * @param chartDto
     * @return
     */
    public PageData listChartPage(ChartDto chartDto);


    /**
     * 创建图
     * @param chartDto
     * @return
     */
    public BaseResult<String> createChart(ChartDto chartDto);

    /**
     * 获取图
     * @param chartId
     * @return
     */
    public BaseResult<ChartDto> queryChart(String chartId);


    public BaseResult<String>  updateChart(ChartDto boardDto);

    /**
     * 删除图
     * @param chartId
     * @return
     */
    public BaseResult<String> delChart(String chartId);
}
