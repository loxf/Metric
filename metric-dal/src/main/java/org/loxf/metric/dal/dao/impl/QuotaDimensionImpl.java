package org.loxf.metric.dal.dao.impl;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionDao;
import org.loxf.metric.dal.po.QuotaDimension;
import org.loxf.metric.dal.po.QuotaDimension;
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
    private static String quota_dim_prefix = "QUOTA_DIMENSION";

    @Override
    public String insert(QuotaDimension object) {
        String sid = IdGenerator.generate(quota_dim_prefix);
        object.setDimCode(sid);
        object.handleDateToMongo();
        super.insert(object, collectionName);
        return sid;
    }

    @Override
    public QuotaDimension findOne(Map<String, Object> params) {
        QuotaDimension quotaDimension= super.findOne(params, collectionName);
        quotaDimension.handleMongoDateToJava();
        return quotaDimension;
    }

    private void handleDateForList(List<QuotaDimension> list){
        for(QuotaDimension quotaDimension:list){
            quotaDimension.handleMongoDateToJava();
        }
    }
    @Override
    public List<QuotaDimension> findAll(Map<String, Object> params) {
        List<QuotaDimension> quotaDimensionList=super.findAll(params, collectionName);
        handleDateForList(quotaDimensionList);
        return quotaDimensionList;
    }

    @Override
    public List<QuotaDimension> findByPager(Map<String, Object> params, int start, int pageSize) {
        List<QuotaDimension> quotaDimensionList=super.findByPager(params, start, pageSize, collectionName);
        handleDateForList(quotaDimensionList);
        return quotaDimensionList;
    }

    @Override
    public void update(Map<String, Object> queryParams, Map<String, Object> setParams) {
        super.update(queryParams, setParams, collectionName);
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
    public long countByParams(Map<String, Object> params) {
        return super.countByParams(params,collectionName);
    }

}
