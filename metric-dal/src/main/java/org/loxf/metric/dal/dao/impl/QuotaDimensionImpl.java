package org.loxf.metric.dal.dao.impl;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionDao;
import org.loxf.metric.dal.po.QuotaDimension;
import org.loxf.metric.dal.po.QuotaDimension;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/4.
 */
@Service("quotaDimension")
public class QuotaDimensionImpl extends MongoDaoBase<QuotaDimension> implements QuotaDimensionDao{
    private final String collectionName = CollectionConstants.QUOTA_DIMENSION.getCollectionName();
    private static String quota_dim_prefix = "DIM_";

    @Override
    public String insert(QuotaDimension object) {
        String sid = IdGenerator.generate(quota_dim_prefix);
        object.setDimCode(sid);
        super.insert(object, collectionName);
        return sid;
    }

    @Override
    public QuotaDimension findOne(QuotaDimension object) {
        QuotaDimension quotaDimension= super.findOne(getCommonQuery(object), collectionName);
        return quotaDimension;
    }

    @Override
    public List<QuotaDimension> findAll(QuotaDimension object) {
        List<QuotaDimension> quotaDimensionList=super.findAll(getCommonQuery(object), collectionName);
        return quotaDimensionList;
    }

    @Override
    public List<QuotaDimension> findByPager(QuotaDimension object, int start, int pageSize) {
        List<QuotaDimension> quotaDimensionList=super.findByPager(getCommonQuery(object), start, pageSize, collectionName);
        return quotaDimensionList;
    }

    @Override
    public void update(QuotaDimension object, Map<String, Object> setParams) {
        super.update(getCommonQuery(object), setParams, collectionName);
    }

    @Override
    public void updateOne(String itemCode, Map<String, Object> setParams) {
        Map<String, Object> queryParams=new HashedMap();
        queryParams.put("quotaDimCode",itemCode);
        super.updateOne(queryParams, setParams, collectionName);
    }

    @Override
    public void remove(String itemCode) {
        Map<String, Object> params=new HashedMap();
        params.put("quotaDimCode",itemCode);
        super.remove(params, collectionName);
    }

    @Override
    public long countByParams(QuotaDimension object) {
        return super.countByParams(getCommonQuery(object),collectionName);
    }

    private Query getCommonQuery(QuotaDimension quotaDimension){
        //TODO 实现各个dao自己的query
        return null;
    }
}
