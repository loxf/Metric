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
        super.insert(quota, collectionName);
        return quota.getQuotaCode();
    }

    @Override
    public Quota findOne(Quota param) {
        return super.findOne(getCommonQuery(param), collectionName);
    }

    @Override
    public List<Quota> findAll(Quota quota) {
        List<Quota> quotaList=super.findAll(getCommonQuery(quota), collectionName);
        return quotaList;
    }

    @Override
    public List<Quota> findByPager(Quota quota, int start, int pageSize) {
        List<Quota> quotaList=super.findByPager(getCommonQuery(quota), start, pageSize, collectionName);
        return quotaList;
    }

    @Override
    public long countByParams(Quota quota) {
        return super.countByParams(getCommonQuery(quota), collectionName);
    }

    @Override
    public void update(Quota quota, Map<String, Object> setParams) {
        super.update(getCommonQuery(quota), setParams, collectionName);
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

    private Query getCommonQuery(Quota quota){
        BasicDBObject query = new BasicDBObject();
        if(StringUtils.isNotEmpty(quota.getQuotaCode())){
            query.put("quotaCode", quota.getQuotaCode());
        }
        if(StringUtils.isNotEmpty(quota.getQuotaName())){
            Pattern pattern = Pattern.compile("^.*" + quota.getQuotaName() +".*$", Pattern.CASE_INSENSITIVE);
            query.put("quotaName", pattern);
        }
        if(StringUtils.isNotEmpty(quota.getQuotaSource())){
            Pattern pattern = Pattern.compile("^.*" + quota.getQuotaSource() +".*$", Pattern.CASE_INSENSITIVE);
            query.put("quotaSource", pattern);
        }
        if(StringUtils.isNotEmpty(quota.getUniqueCode())){
            query.put("uniqueCode", quota.getUniqueCode());
        }
        if(StringUtils.isNotEmpty(quota.getType())){
            query.put("type", quota.getType());
        }
        if(StringUtils.isNotEmpty(quota.getShowType())){
            query.put("showType", quota.getShowType());
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
}
