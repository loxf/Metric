package org.loxf.metric.dal.dao.impl;

import org.apache.commons.lang.StringUtils;

import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.QuotaDao;
import org.loxf.metric.dal.po.Quota;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/6/26.
 */
@Service("quotaDao")
public class QuotaDaoImpl extends MongoDaoBase<Quota> implements QuotaDao{
    private static String quota_prefix = "QUOTA_";
    private static Logger logger = LoggerFactory.getLogger(QuotaDaoImpl.class);

    private final String collectionName = CollectionConstants.QUOTA.name();

//    @Override
//    public Quota selectQuota(Quota quota){
//        Query query = getWhere(quota);
//        return mongoTemplate.findOne(query, Quota.class, collectionName);
//    }
//
//    @Override
//    public List<Quota> selectList(Quota quota){
//        Query query = getWhere(quota);
//        return mongoTemplate.find(query, Quota.class, collectionName);
//    }
//
//    public long count(Quota quota){
//        Query query = getWhere(quota);
//        return mongoTemplate.count(query, Quota.class, collectionName);
//    }

//    private Query getWhere(Quota quota){
//        Query query = new Query();
//        Criteria criteria = Criteria.where("1").is("1");
//        if(StringUtils.isNotEmpty(quota.getQuotaId())){
//            criteria = criteria.and("quotaId").is(quota.getQuotaId());
//        }
//        if(StringUtils.isNotEmpty(quota.getQuotaCode())){
//            criteria = criteria.and("quotaCode").is(quota.getQuotaCode());
//        }
//        if(StringUtils.isNotEmpty(quota.getQuotaSource())){
//            criteria = criteria.and("quotaSource").is(quota.getQuotaSource());
//        }
//        if(StringUtils.isNotEmpty(quota.getQuotaName())){
//            criteria = criteria.and("quotaName").is(quota.getQuotaName());
//        }
//        if(StringUtils.isNotEmpty(quota.getQuotaDisplayName())){
//            criteria = criteria.and("quotaDisplayName").regex(".*?\\" +quota.getQuotaDisplayName()+ ".*");
//        }
//        if(StringUtils.isNotEmpty(quota.getType())){
//            criteria = criteria.and("type").is(quota.getType());
//        }
//        if( quota.getState()>=0){
//            criteria = criteria.and("state").is(quota.getState());
//        }
//        if(StringUtils.isNotEmpty(quota.getCreatedAtStart())){
//            criteria = criteria.and("createdAtStart").gte(quota.getCreatedAtStart());
//        }
//        if(StringUtils.isNotEmpty(quota.getCreatedAtEnd())){
//            criteria = criteria.and("createdAtEnd").lte(quota.getCreatedAtEnd());
//        }
//        query.addCriteria(criteria);
//        return query;
//    }

    @Override
    public String insert(Quota object) {
        String sid = IdGenerator.generate(quota_prefix);
        object.setQuotaCode(sid);
        object.handleDateToMongo();
        super.insert(object, collectionName);
        return sid;
    }

    @Override
    public Quota findOne(Map<String, Object> params) {
        Quota quota= super.findOne(params, collectionName);
        quota.handleMongoDateToJava();
        return quota;
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
    public void updateOne(Map<String, Object> queryParams, Map<String, Object> setParams) {
        super.updateOne(queryParams, setParams, collectionName);
    }

    @Override
    public void remove(Map<String, Object> params) {
        super.remove(params, collectionName);
    }

    @Override
    public long countByParams(Map<String, Object> params) {
        return super.countByParams(params,collectionName);
    }

}
