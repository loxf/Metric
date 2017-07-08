package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.ChartDao;
import org.loxf.metric.dal.po.Chart;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/4.
 */
@Service("chartDao")
public class ChartDaoImpl extends MongoDaoBase<Chart> implements ChartDao{
    private final String collectionName = CollectionConstants.CHART.name();
    private static String chart_prefix = "CHART_";

    @Override
    public String insert(Chart object) {
        String sid = IdGenerator.generate(chart_prefix);
        object.setChartCode(sid);
        object.handleDateToMongo();
        super.insert(object, collectionName);
        return sid;
    }

    @Override
    public Chart findOne(Map<String, Object> params) {
        Chart chart= super.findOne(params, collectionName);
        chart.handleMongoDateToJava();
        return chart;
    }

    private void handleDateForList(List<Chart> list){
        for(Chart chart:list){
            chart.handleMongoDateToJava();
        }
    }
    @Override
    public List<Chart> findAll(Map<String, Object> params) {
        List<Chart> chartList=super.findAll(params, collectionName);
        handleDateForList(chartList);
        return chartList;
    }

    @Override
    public List<Chart> findByPager(Map<String, Object> params, int start, int pageSize) {
        List<Chart> chartList=super.findByPager(params, start, pageSize, collectionName);
        handleDateForList(chartList);
        return chartList;
    }

    @Override
    public void update(Map<String, Object> queryParams, Map<String, Object> setParams) {
        super.update(queryParams, setParams, collectionName);
    }

    @Override
    public void updateOne(Map<String, Object> queryParams, Map<String, Object> setParams) {
        super.updateOne(queryParams, setParams, collectionName);
    }

    @Override
    public void remove(Map<String, Object> params) {
        super.remove(params, collectionName);
    }

    @Override
    public long countByParams(Map<String, Object> params) {
        return super.countByParams(params,collectionName);
    }

}

