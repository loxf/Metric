package org.loxf.metric.dal.dao.impl;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.ChartDao;
import org.loxf.metric.dal.po.Chart;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/4.
 */
@Service("chartDao")
public class ChartDaoImpl extends MongoDaoBase<Chart> implements ChartDao{
    private final String collectionName = CollectionConstants.CHART.getCollectionName();
    private static String chart_prefix = "CHART_";

    @Override
    public String insert(Chart object) {
        String sid = IdGenerator.generate(chart_prefix);
        object.setChartCode(sid);
        super.insert(object, collectionName);
        return sid;
    }

    @Override
    public Chart findOne(Chart params) {
        Chart chart= super.findOne(getCommonQuery(params), collectionName);
        return chart;
    }

    @Override
    public List<Chart> findAll(Chart params) {
        List<Chart> chartList=super.findAll(getCommonQuery(params), collectionName);
        return chartList;
    }

    @Override
    public List<Chart> findByPager(Chart params, int start, int pageSize) {
        List<Chart> chartList=super.findByPager(getCommonQuery(params), start, pageSize, collectionName);
        return chartList;
    }

    @Override
    public long countByParams(Chart params) {
        return super.countByParams(getCommonQuery(params),collectionName);
    }

    @Override
    public void update(Chart params, Map<String, Object> setParams) {
        super.update(getCommonQuery(params), setParams, collectionName);
    }

    @Override
    public void updateOne(String itemCode, Map<String, Object> setParams) {
        Map<String, Object> queryParams=new HashedMap();
        queryParams.put("chartCode",itemCode);
        super.updateOne(queryParams, setParams, collectionName);
    }

    @Override
    public void remove(String itemCode) {
        Map<String, Object> params=new HashedMap();
        params.put("chartCode",itemCode);
        super.remove(params, collectionName);
    }

    private Query getCommonQuery(Chart chart){
        //TODO 实现各个dao自己的query
        return null;
    }
}

