package org.loxf.metric.service.impl;


import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.service.base.BaseService;
import org.loxf.metric.client.IChartService;
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
import java.util.Map;

/**
 * 图配置业务类
 * Created by caiyang on 2017/5/4.
 */
@Service("chartService")
public class ChartServiceImpl extends BaseService implements IChartService {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private ChartManager chartManager;


    @Override
    public PageData getPageList(ChartDto chartDto) {
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartDto, chart);
        PageData pageUtilsUI = null;//super.pageList(chart,ChartMapper.class,"Chart");
        List<Chart> chartList = pageUtilsUI.getRows();
        List<ChartDto> chartDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(chartList)) {
            for (Chart c : chartList) {
                ChartDto dto = new ChartDto();
                BeanUtils.copyProperties(c, dto);
                chartDtoList.add(dto);
            }
        }
        pageUtilsUI.setRows(chartDtoList);
        return pageUtilsUI;
    }

    @Override
    public BaseResult<String> insertIterm(ChartDto chartDto) {
        try {
            return new BaseResult<String>(chartManager.insert(chartDto));
        } catch (Exception e) {
            logger.error("创建图失败", e);
            throw new MetricException("创建图失败", e);
        }
    }

    public BaseResult<ChartDto> queryItemByCode(String chartCode) {
        Map qryParams=new HashedMap();
        qryParams.put("chartCode",chartCode);
        return new BaseResult<>(chartManager.getChartDtoByParams(qryParams));
    }

    @Override
    public BaseResult<String> updateItem(String chartCode,Map<String, Object> setParams) {
        try {
            chartManager.updateChartByCode(chartCode,setParams);

        } catch (Exception e) {
            logger.error("创建图失败", e);
            throw new MetricException("创建图失败", e);
        }
        return new BaseResult<String>(chartCode);
    }

    @Override
    public BaseResult<String> delItemByCode(String chartCode) {
        try {
            chartManager.delChartByCode(chartCode);

        } catch (Exception e) {
            logger.error("创建图失败", e);
            throw new MetricException("创建图失败", e);
        }
        return new BaseResult<String>(chartCode);
    }
}
