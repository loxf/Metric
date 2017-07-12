package org.loxf.metric.dal.dao.impl;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionValueDao;
import org.loxf.metric.dal.po.QuotaDimensionValue;
import org.loxf.metric.dal.po.QuotaDimensionValue;
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
    private static String quota_dim_value_prefix = "QUOTA_DIM_VALUE";

    @Override
    public String insert(QuotaDimensionValue object) {
        String sid = IdGenerator.generate(quota_dim_value_prefix);
        object.setDimValueCode(sid);
        object.handleDateToMongo();
        super.insert(object, collectionName);
        return sid;
    }

    @Override
    public QuotaDimensionValue findOne(Map<String, Object> params) {
        QuotaDimensionValue quotaDimensionValue= super.findOne(params, collectionName);
        quotaDimensionValue.handleMongoDateToJava();
        return quotaDimensionValue;
    }

    private void handleDateForList(List<QuotaDimensionValue> list){
        for(QuotaDimensionValue quotaDimensionValue:list){
            quotaDimensionValue.handleMongoDateToJava();
        }
    }
    @Override
    public List<QuotaDimensionValue> findAll(Map<String, Object> params) {
        List<QuotaDimensionValue> quotaDimensionValueList=super.findAll(params, collectionName);
        handleDateForList(quotaDimensionValueList);
        return quotaDimensionValueList;
    }

    @Override
    public List<QuotaDimensionValue> findByPager(Map<String, Object> params, int start, int pageSize) {
        List<QuotaDimensionValue> QuotaDimensionValueList=super.findByPager(params, start, pageSize, collectionName);
        handleDateForList(QuotaDimensionValueList);
        return QuotaDimensionValueList;
    }

    @Override
    public void update(Map<String, Object> queryParams, Map<String, Object> setParams) {
        super.update(queryParams, setParams, collectionName);
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
    public long countByParams(Map<String, Object> params) {
        return super.countByParams(params,collectionName);
    }

}
