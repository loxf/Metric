package org.loxf.metric.service.callable;

import org.loxf.metric.api.IDataQueryService;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartData;
import org.loxf.metric.common.dto.ConditionVo;
import org.loxf.metric.dal.dao.interfaces.DataDao;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionValueDao;
import org.loxf.metric.service.base.SpringApplicationContextUtil;
import org.loxf.metric.service.impl.DataQueryServiceImpl;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by luohj on 2017/7/15.
 */
public class QueryChartDataCallable implements Callable<ChartData> {
    private IDataQueryService service;
    private String handleUserName;
    private String chartCode;
    private ConditionVo condition;
    public QueryChartDataCallable(String handleUserName, String chartCode, ConditionVo condition, IDataQueryService service){
        this.handleUserName = handleUserName;
        this.chartCode = chartCode;
        this.condition = condition;
        this.service = service;
    }
    @Override
    public ChartData call() throws Exception {
        return this.service.getChartData(handleUserName, chartCode, condition);
    }
}
