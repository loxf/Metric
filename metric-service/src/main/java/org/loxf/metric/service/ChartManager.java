package org.loxf.metric.service;

import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.common.dto.ChartQuotaRelDto;
import org.loxf.metric.common.dto.QuotaDto;
import org.loxf.metric.dal.dao.interfaces.ChartDao;
import org.loxf.metric.dal.po.Chart;
import org.loxf.metric.dal.po.ChartQuotaRel;
import org.loxf.metric.dal.po.QuotaWithDim;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by caiyang on 2017/5/4.
 */
@Component
public class ChartManager {
    private static String chart_prefix = "CHT_";
    @Autowired
    private ChartDao chartDao;

    @Transactional(rollbackFor = Exception.class)
    public String insert(ChartDto chartDto){
        Chart chart=new Chart();
        BeanUtils.copyProperties(chartDto,chart);
        String sid = IdGenerator.generate(chart_prefix);
        chart.setChartId(sid);
        chart.setCreatedAt(new Date());
        chart.setUpdatedAt(new Date());
        chartDao.insert(chart);
        return sid;
    }

    public ChartDto getChart(String chartId){
        ChartDto chartDto = new ChartDto();
        /*Chart chart = chartDao.selectByChartId(chartId);
        BeanUtils.copyProperties(chart, chartDto);*/
        return chartDto;
    }
    @Transactional
    public BaseResult<String> updateChart(ChartDto chartDto) {
        Chart chart=new Chart();
        BeanUtils.copyProperties(chartDto, chart);
        // chartDao.update(chart);
        return new BaseResult<>();
    }
    @Transactional
    public BaseResult<String> delChart(String chartId) {
        Map map = new HashMap<>();
        map.put("chartId", chartId);
        chartDao.remove(map);
        return new BaseResult<>();
    }
}
