package org.loxf.metric.biz.impl;


import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.biz.base.BaseService;
import org.loxf.metric.client.ChartService;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.dal.po.Chart;
import org.loxf.metric.service.ChartManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 图配置业务类
 * Created by caiyang on 2017/5/4.
 */
@Service("chartService")
public class ChartServiceImpl extends BaseService implements ChartService{
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private ChartManager chartManager;


    @Override
    public PageData listChartPage(ChartDto chartDto) {
        Chart chart=new Chart();
        BeanUtils.copyProperties(chartDto,chart);
        PageData pageUtilsUI=super.pageList(chart,ChartMapper.class,"Chart");
        List<Chart> chartList=    pageUtilsUI.getRows();
        List<ChartDto> chartDtoList=new ArrayList<>();
        if(!CollectionUtils.isEmpty(chartList)){
            for(Chart c:chartList){
                ChartDto dto=new ChartDto();
                BeanUtils.copyProperties(c, dto);
                chartDtoList.add(dto);
            }
        }
        pageUtilsUI.setRows(chartDtoList);
        return  pageUtilsUI;
    }

    @Override
    public BaseResult<String> createChart(ChartDto chartDto) {
        try {
            return new BaseResult<String>(chartManager.insert(chartDto));
        } catch (Exception e){
            logger.error("创建图失败", e);
            throw new MetricException("创建图失败", e);
        }
    }

    public BaseResult<ChartDto> queryChart(String chartId){
        try{
            return new BaseResult<>(chartManager.getChart(chartId));
        } catch (Exception e){
            logger.error("获取图失败:"+chartId, e);
            throw new MetricException("获取图失败:"+chartId, e);
        }
    }

    @Override
    public BaseResult<String> updateChart(ChartDto chartDto) {
         return chartManager.updateChart(chartDto);
    }

    @Override
    public BaseResult<String> delChart(String chartId) {
        return chartManager.delChart(chartId);
    }
}
