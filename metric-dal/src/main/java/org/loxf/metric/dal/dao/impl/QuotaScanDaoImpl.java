package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.QuotaScanDao;
import org.loxf.metric.dal.po.QuotaScan;
import org.loxf.metric.dal.po.QuotaScan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/4.
 */
@Component("quotaScanDaoImpl")
public class QuotaScanDaoImpl extends MongoDaoBase<QuotaScan> implements QuotaScanDao{
    private final String collectionName = "quota_scan";

    @Override
    public void insert(QuotaScan object) {
        super.insert(object, collectionName);
    }

    @Override
    public QuotaScan findOne(Map<String, Object> params) {
        return super.findOne(params, collectionName);
    }

    @Override
    public List<QuotaScan> findAll(Map<String, Object> params) {
        return super.findAll(params, collectionName);
    }

    @Override
    public List<QuotaScan> findByPager(Map<String, Object> params, int start, int end) {
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
