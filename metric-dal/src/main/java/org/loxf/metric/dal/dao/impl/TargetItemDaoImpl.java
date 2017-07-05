package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.TargetItemDao;
import org.loxf.metric.dal.po.TargetItem;
import org.loxf.metric.dal.po.TargetItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/4.
 */
@Component("targetItemDaoImpl")
public class TargetItemDaoImpl extends MongoDaoBase<TargetItem> implements TargetItemDao{
    private final String collectionName = "target_item";

    @Override
    public void insert(TargetItem object) {
        super.insert(object, collectionName);
    }

    @Override
    public TargetItem findOne(Map<String, Object> params) {
        return super.findOne(params, collectionName);
    }

    @Override
    public List<TargetItem> findAll(Map<String, Object> params) {
        return super.findAll(params, collectionName);
    }

    @Override
    public List<TargetItem> findByPager(Map<String, Object> params, int start, int end) {
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
