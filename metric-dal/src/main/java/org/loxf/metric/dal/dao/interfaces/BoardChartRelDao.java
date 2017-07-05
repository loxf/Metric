package org.loxf.metric.dal.dao.interfaces;


import org.loxf.metric.core.mongo.MongoBase;
import org.loxf.metric.dal.po.BoardChartRel;
import org.loxf.metric.dal.po.Chart;

import java.util.List;
import java.util.Map;

public interface BoardChartRelDao extends MongoBase<BoardChartRel> {
    public void insert(BoardChartRel object);

    public BoardChartRel findOne(Map<String, Object> params);

    public List<BoardChartRel> findAll(Map<String, Object> params);

    public List<BoardChartRel> findByPager(Map<String, Object> params, int start, int end);

    public void update(Map<String, Object> queryParams, Map<String, Object> setParams);

    public void updateOne(Map<String, Object> queryParams, Map<String, Object> setParams);

    public void createCollection();

    public void remove(Map<String, Object> params);
}