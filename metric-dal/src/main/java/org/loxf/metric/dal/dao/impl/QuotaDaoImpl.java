package org.loxf.metric.dal.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import org.loxf.metric.base.ItemList.QuotaDimItem;
import org.loxf.metric.base.ItemList.TargetItem;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.QuotaDao;
import org.loxf.metric.dal.po.Quota;
import org.loxf.metric.dal.po.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by luohj on 2017/6/26.
 */
@Service("quotaDao")
public class QuotaDaoImpl extends MongoDaoBase<Quota> implements QuotaDao{
    private final String collectionName = CollectionConstants.QUOTA.getCollectionName();

    @Override
    public String insert(Quota quota) {
        quota.setCreatedAt(new Date());
        quota.setUpdatedAt(new Date());
        quota.handleDateToMongo();
        super.insert(quota, collectionName);
        return quota.getQuotaCode();
    }

    @Override
    public Quota findOne(Map<String, Object> params) {
        Quota quota= super.findOne(params, collectionName);
        if(quota!=null) {
            quota.handleMongoDateToJava();
            return quota;
        }
        return null;
    }

    private void handleDateForList(List<Quota> list){
        for(Quota quota:list){
            quota.handleMongoDateToJava();
        }
    }
    @Override
    public List<Quota> findAll(Map<String, Object> params) {
        List<Quota> quotaList=super.findAll(params, collectionName);
        handleDateForList(quotaList);
        return quotaList;
    }

    @Override
    public List<Quota> findByPager(Map<String, Object> params, int start, int pageSize) {
        List<Quota> quotaList=super.findByPager(params, start, pageSize, collectionName);
        handleDateForList(quotaList);
        return quotaList;
    }

    @Override
    public void update(Map<String, Object> queryParams, Map<String, Object> setParams) {
        super.update(queryParams, setParams, collectionName);
    }

    @Override
    public void updateOne(String itemCode, Map<String, Object> setParams) {
        Map<String, Object> queryParams=new HashedMap();
        queryParams.put("quotaCode",itemCode);
        super.updateOne(queryParams, setParams, collectionName);
    }

    @Override
    public void remove(String itemCode) {
        Map<String, Object> params=new HashedMap();
        params.put("quotaCode",itemCode);
        // 如果删除基础指标，需先删除对应的数据集合，再删除指标。
        super.remove(params, collectionName);
    }

    @Override
    public long countByParams(Map<String, Object> params) {
        return super.countByParams(params, collectionName);
    }


    private Query getCommonQuery(Quota quota){
        BasicDBObject query = new BasicDBObject();
        if(StringUtils.isNotEmpty(quota.getQuotaCode())){
            query.put("quotaCode", quota.getQuotaCode());
        }
        if(StringUtils.isNotEmpty(quota.getQuotaName())){
            Pattern pattern = Pattern.compile("^.*" + quota.getQuotaName() +".*$", Pattern.CASE_INSENSITIVE);
            query.put("quotaName", pattern);
        }
        if(StringUtils.isNotEmpty(quota.getUniqueCode())){
            query.put("uniqueCode", quota.getUniqueCode());
        }
        if(StringUtils.isNotEmpty(quota.getType())){
            query.put("type", quota.getType());
        }
        if(StringUtils.isNotEmpty(quota.getShowOperation())){
            query.put("showOperation", quota.getShowOperation());
        }
        if(StringUtils.isNotEmpty(quota.getCreateUserName())){
            query.put("createUserName", quota.getCreateUserName());
        }
        if(StringUtils.isNotEmpty(quota.getDataImportType())){
            query.put("dataImportType", quota.getDataImportType());
        }
        if(quota.getStartDate()!=null||quota.getEndDate()!=null){
            Map<String, Object> createT = new HashMap<>();
            if(quota.getStartDate()!=null)
                createT.put("$gte", quota.getStartDate());
            if(quota.getEndDate()!=null)
                createT.put("$lte", quota.getEndDate());
            query.put("createdAt", createT);
        }
        if(CollectionUtils.isNotEmpty(quota.getQuotaDim())){
            BasicDBList includeDims = new BasicDBList();
            for(QuotaDimItem item : quota.getQuotaDim()){
                includeDims.add(item.getDimCode());
            }
            query.put("quotaDim.dimCode", new BasicDBObject("$in", includeDims));
        }
        return new BasicQuery(query);
    }

    @Override
    public List<Quota> findAllByQuery(Quota quota) {
        List<Quota> quotaList = super.findAll(getCommonQuery(quota), collectionName);
        handleDateForList(quotaList);
        return quotaList;
    }
}
