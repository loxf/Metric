package org.loxf.metric.dal.dao;


import org.loxf.metric.dal.po.Chart;

import java.util.List;

public interface ChartMapper {
    int deleteByChartId(String chartId);

    int insert(Chart record);

    Chart selectByChartId(String id);

    List<Chart> listChart(Chart chart);

    int countChart(Chart chart);

    int updateChart(Chart chart);
}