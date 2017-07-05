package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionDao;
import org.loxf.metric.dal.po.QuotaDimension;
import org.loxf.metric.dal.po.QuotaDimension;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/4.
 */
@Component("quotaDimensionImpl")
public class QuotaDimensionImpl extends MongoDaoBase<QuotaDimension> implements QuotaDimensionDao{
    private final String collectionName = "quota_dimension";

    @Override
    public void insert(QuotaDimension object) {
        super.insert(object, collectionName);
    }

    @Override
    public QuotaDimension findOne(Map<String, Object> params) {
        return super.findOne(params, collectionName);
    }

    @Override
    public List<QuotaDimension> findAll(Map<String, Object> params) {
        return super.findAll(params, collectionName);
    }

    @Override
    public List<QuotaDimension> findByPager(Map<String, Object> params, int start, int end) {
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
    public void createCollection() {
        super.createCollection(collectionName);
    }

    @Override
    public void remove(Map<String, Object> params) {
        super.remove(params, collectionName);
    }
}
