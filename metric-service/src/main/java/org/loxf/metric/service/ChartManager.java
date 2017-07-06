package org.loxf.metric.service;

import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.common.dto.ChartQuotaRelDto;
import org.loxf.metric.common.dto.QuotaDto;
import org.loxf.metric.dal.po.Chart;
import org.loxf.metric.dal.po.ChartQuotaRel;
import org.loxf.metric.dal.po.QuotaWithDim;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by caiyang on 2017/5/4.
 */
@Component
public class ChartManager {
    private static String chart_prefix = "CHT_";
    @Autowired
    private ChartMapper chartMapper;
    @Autowired
    private ChartQuotaRelMapper chartQuotaRelMapper;
    @Transactional(rollbackFor = Exception.class)
    public String insert(ChartDto chartDto){
        Chart chart=new Chart();
        BeanUtils.copyProperties(chartDto,chart);
        String sid = IdGenerator.generate(chart_prefix);
        chart.setChartId(sid);
        chart.setCreatedAt(new Date());
        chart.setUpdatedAt(new Date());
        chartMapper.insert(chart);
        List<ChartQuotaRelDto> quotaRelList=  chartDto.getQuotaRelList();
        if(CollectionUtils.isNotEmpty(quotaRelList)){
            for(ChartQuotaRelDto quotaDto: quotaRelList) {
                ChartQuotaRel  chartQuotaRel = new ChartQuotaRel();
                chartQuotaRel.setChartId(sid);
                chartQuotaRel.setQuotaId(quotaDto.getQuotaId());
                chartQuotaRel.setQuotaDimension(quotaDto.getQuotaDimension());
                chartQuotaRelMapper.insert(chartQuotaRel);
            }
        }
        return sid;
    }

    public ChartDto getChart(String chartId){
        ChartDto chartDto = new ChartDto();
        Chart chart = chartMapper.selectByChartId(chartId);
        if(chart!=null){
            BeanUtils.copyProperties(chart, chartDto);
            // 获取图对应的指标
            List<QuotaWithDim> quotaList = chartQuotaRelMapper.selectByChartId(chartId);
            if(CollectionUtils.isNotEmpty(quotaList)){
                List<QuotaDto> quotaDtoList= new ArrayList<>();
                for(QuotaWithDim quota: quotaList){
                    QuotaDto dto = new QuotaDto();
                    BeanUtils.copyProperties(quota, dto);
                    quotaDtoList.add(dto);
                }
                chartDto.setQuotaList(quotaDtoList);
            }
        }
        return chartDto;
    }
    @Transactional
    public BaseResult<String> updateChart(ChartDto chartDto) {
        Chart chart=new Chart();
        BeanUtils.copyProperties(chartDto, chart);
        int i=  chartMapper.updateChart(chart);
        if(i>0){
            chartQuotaRelMapper.deleteByChartId(chartDto.getChartId());
            List<QuotaDto> quotaDtos=  chartDto.getQuotaList();
            if(CollectionUtils.isNotEmpty(quotaDtos)){
                for(QuotaDto quotaDto: quotaDtos) {
                    ChartQuotaRel  chartQuotaRel = new ChartQuotaRel();
                    chartQuotaRel.setChartId(chartDto.getChartId());
                    chartQuotaRel.setQuotaId(quotaDto.getQuotaId());
                    chartQuotaRelMapper.insert(chartQuotaRel);
                }
            }
        }
        return new BaseResult<>(i+"");
    }
    @Transactional
    public BaseResult<String> delChart(String chartId) {
        int i=  chartMapper.deleteByChartId(chartId);
        if(i>0){
            int j= chartQuotaRelMapper.deleteByChartId(chartId);
             return new BaseResult<>(i+"");

        }
        return new BaseResult<>(0,-1+"");
    }
}
