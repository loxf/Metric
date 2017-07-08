package org.loxf.metric.service;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.dal.dao.interfaces.ChartDao;
import org.loxf.metric.dal.po.Chart;
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

    public String insert(ChartDto chartDto){
        Chart chart=new Chart();
        BeanUtils.copyProperties(chartDto,chart);
        chart.setCreatedAt(new Date());
        chart.setUpdatedAt(new Date());
        return chartDao.insert(chart);
    }

    public ChartDto getChartDtoByParams(Map<String, Object> params){
        Chart chart=getChartByParams(params);
        ChartDto chartDto=new ChartDto();
        BeanUtils.copyProperties(chart, chartDto);
        return chartDto;
    }

    public Chart getChartByParams(Map<String, Object> params){
        return chartDao.findOne(params);
    }
    public void updateChartByCode(String chartCode,Map<String, Object> setParams) {
        Map<String, Object> qryParams=new HashedMap();
        qryParams.put("chartCode",chartCode);
        setParams.put("updatedAt",new Date());
        chartDao.update(qryParams,setParams);
    }
    public void delChartByCode(String chartCode) {
        Map<String, Object> qryParams=new HashedMap();
        qryParams.put("chartCode",chartCode);
        chartDao.remove(qryParams);
    }

    public List<Chart> getPageList(Map<String, Object> qryParams,int pageNum,int pageSize){
        int start=(pageNum-1)*pageSize;
        return chartDao.findByPager(qryParams,  start,  pageSize) ;
    }
}
