package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.ChartDao;
import org.loxf.metric.dal.po.Chart;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/4.
 */
@Component("chartDaoImpl")
public class ChartDaoImpl extends MongoDaoBase<Chart> implements ChartDao{
    private final String collectionName = "chart";

    @Override
    public void insert(Chart object) {
        super.insert(object, collectionName);
    }

    @Override
    public Chart findOne(Map<String, Object> params) {
        return super.findOne(params, collectionName);
    }

    @Override
    public List<Chart> findAll(Map<String, Object> params) {
        return super.findAll(params, collectionName);
    }

    @Override
    public List<Chart> findByPager(Map<String, Object> params, int start, int end) {
        return super.findByPager(params, start, end, collectionName);
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
}
