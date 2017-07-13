package org.loxf.metric.dal.dao.impl;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionValueDao;
import org.loxf.metric.dal.po.QuotaDimensionValue;
import org.loxf.metric.dal.po.QuotaDimensionValue;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/4.
 */
@Service("quotaDimensionValue")
public class QuotaDimensionValueImpl extends MongoDaoBase<QuotaDimensionValue> implements QuotaDimensionValueDao{
    private final String collectionName = CollectionConstants.QUOTA_DIMENSION_VALUE.getCollectionName();
    private static String quota_dim_value_prefix = "DIM_V_";

    @Override
    public String insert(QuotaDimensionValue object) {
        String sid = IdGenerator.generate(quota_dim_value_prefix);
        object.setDimValueCode(sid);
        super.insert(object, collectionName);
        return sid;
    }

    @Override
    public QuotaDimensionValue findOne(QuotaDimensionValue object) {
        QuotaDimensionValue quotaDimensionValue= super.findOne(getCommonQuery(object), collectionName);
        return quotaDimensionValue;
    }

    @Override
    public List<QuotaDimensionValue> findAll(QuotaDimensionValue object) {
        List<QuotaDimensionValue> quotaDimensionValueList=super.findAll(getCommonQuery(object), collectionName);
        return quotaDimensionValueList;
    }

    @Override
    public List<QuotaDimensionValue> findByPager(QuotaDimensionValue object, int start, int pageSize) {
        List<QuotaDimensionValue> QuotaDimensionValueList=super.findByPager(getCommonQuery(object), start, pageSize, collectionName);
        return QuotaDimensionValueList;
    }

    @Override
    public void update(QuotaDimensionValue object, Map<String, Object> setParams) {
        super.update(getCommonQuery(object), setParams, collectionName);
    }

    @Override
    public void updateOne(String itemCode, Map<String, Object> setParams) {
        Map<String, Object> queryParams=new HashedMap();
        queryParams.put("quotaDimValueCode",itemCode);
        super.updateOne(queryParams, setParams, collectionName);
    }

    @Override
    public void remove(String itemCode) {
        Map<String, Object> params=new HashedMap();
        params.put("quotaDimValueCode",itemCode);
        super.remove(params, collectionName);
    }

    @Override
    public long countByParams(QuotaDimensionValue object) {
        return super.countByParams(getCommonQuery(object),collectionName);
    }

    private Query getCommonQuery(QuotaDimensionValue quotaDimensionValue){
        //TODO 实现各个dao自己的query
        return null;
    }

}
