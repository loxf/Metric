package org.loxf.metric.dal.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.core.mongo.MongoBase;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.QuotaDao;
import org.loxf.metric.dal.po.Quota;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/6/26.
 */
@Component("quotaDaoImpl")
public class QuotaDaoImpl extends MongoDaoBase implements QuotaDao {
    private static Logger logger = LoggerFactory.getLogger(QuotaDaoImpl.class);

    private static final String DefaultCollectionName = "Quota";

    public Quota selectQuota(Quota quota){
        Query query = getWhere(quota);
        return mongoTemplate.findOne(query, Quota.class, DefaultCollectionName);
    }

    public List<Quota> selectList(Quota quota){
        Query query = getWhere(quota);
        return mongoTemplate.find(query, Quota.class, DefaultCollectionName);
    }

    public int count(Quota quota)

    private Query getWhere(Quota quota){
        Query query = new Query();
        Criteria criteria = Criteria.where("1").is("1");
        if(StringUtils.isNotEmpty(quota.getQuotaId())){
            criteria = criteria.and("quotaId").is(quota.getQuotaId());
        }
        if(StringUtils.isNotEmpty(quota.getQuotaCode())){
            criteria = criteria.and("quotaCode").is(quota.getQuotaCode());
        }
        if(StringUtils.isNotEmpty(quota.getQuotaSource())){
            criteria = criteria.and("quotaSource").is(quota.getQuotaSource());
        }
        if(StringUtils.isNotEmpty(quota.getQuotaName())){
            criteria = criteria.and("quotaName").is(quota.getQuotaName());
        }
        if(StringUtils.isNotEmpty(quota.getQuotaDisplayName())){
            criteria = criteria.and("quotaDisplayName").regex(".*?\\" +quota.getQuotaDisplayName()+ ".*");
        }
        if(StringUtils.isNotEmpty(quota.getType())){
            criteria = criteria.and("type").is(quota.getType());
        }
        if( quota.getState()>=0){
            criteria = criteria.and("state").is(quota.getState());
        }
        if(StringUtils.isNotEmpty(quota.getCreatedAtStart())){
            criteria = criteria.and("createdAtStart").gte(quota.getCreatedAtStart());
        }
        if(StringUtils.isNotEmpty(quota.getCreatedAtEnd())){
            criteria = criteria.and("createdAtEnd").lte(quota.getCreatedAtEnd());
        }
        query.addCriteria(criteria);
        return query;
    }
}
