package org.loxf.metric.dal.dao;


import org.loxf.metric.dal.po.ChartQuotaRel;
import org.loxf.metric.dal.po.QuotaWithDim;

import java.util.List;

public interface ChartQuotaRelMapper {
    int insert(ChartQuotaRel record);

    List<QuotaWithDim> selectByChartId(String id);

    int deleteByChartId(String chartId);
}