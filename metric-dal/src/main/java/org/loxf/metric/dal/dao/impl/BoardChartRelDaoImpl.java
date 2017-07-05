package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.BoardChartRelDao;
import org.loxf.metric.dal.po.BoardChartRel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/4.
 */
@Component("boardChartRelDaoImpl")
public class BoardChartRelDaoImpl extends MongoDaoBase<BoardChartRel> implements BoardChartRelDao {
    private final String DefaultCollectionName = "BoardChartRel";

    @Override
    public void insert(BoardChartRel object) {
        super.insert(object, DefaultCollectionName);
    }

    @Override
    public BoardChartRel findOne(Map<String, Object> params) {
        return super.findOne(params, DefaultCollectionName);
    }

    @Override
    public List<BoardChartRel> findAll(Map<String, Object> params) {
        return super.findAll(params, DefaultCollectionName);
    }

    @Override
    public List<BoardChartRel> findByPager(Map<String, Object> params, int start, int end) {
        return super.findByPager(params, start, end, DefaultCollectionName);
    }

    @Override
    public void update(Map<String, Object> queryParams, Map<String, Object> setParams) {
        super.update(queryParams, setParams, DefaultCollectionName);
    }

    @Override
    public void updateOne(Map<String, Object> queryParams, Map<String, Object> setParams) {
        super.updateOne(queryParams, setParams, DefaultCollectionName);
    }

    @Override
    public void createCollection() {
        super.createCollection(DefaultCollectionName);
    }

    @Override
    public void remove(Map<String, Object> params) {
        super.remove(params, DefaultCollectionName);
    }
}
