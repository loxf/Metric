package org.loxf.metric.dal.dao.impl;

import com.mongodb.BasicDBObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.DateUtil;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.TargetDao;
import org.loxf.metric.dal.po.Target;
import org.loxf.metric.dal.po.Target;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/4.
 */
@Service("targetDao")
public class TargetDaoImpl extends MongoDaoBase<Target> implements TargetDao{
    private final String collectionName = CollectionConstants.TARGET.getCollectionName();
    private static String target_prefix = "TARGET_";

    @Override
    public String insert(Target object) {
        String sid = IdGenerator.generate(target_prefix);
        object.setTargetCode(sid);
        object.handleDateToMongo();
        super.insert(object, collectionName);
        return  sid;
    }

    @Override
    public Target findOne(Map<String, Object> params) {
        Target target= super.findOne(params, collectionName);
        target.handleMongoDateToJava();
        return target;
    }

    private void handleDateForList(List<Target> list){
        for(Target target:list){
            target.handleMongoDateToJava();
        }
    }
    @Override
    public List<Target> findAll(Map<String, Object> params) {
        List<Target> targetList=super.findAll(params, collectionName);
        handleDateForList(targetList);
        return targetList;
    }

    @Override
    public List<Target> findAllByQuery(Target target) {
        List<Target> targetList=super.findAll(getCommonQuery(target), collectionName);
        handleDateForList(targetList);
        return targetList;
    }

    @Override
    public List<Target> findByPager(Map<String, Object> params, int start, int pageSize) {
        List<Target> targetList=super.findByPager(params, start, pageSize, collectionName);
        handleDateForList(targetList);
        return targetList;
    }

    @Override
    public void update(Map<String, Object> queryParams, Map<String, Object> setParams) {
        super.update(queryParams, setParams, collectionName);
    }

    @Override
    public void updateOne(String itemCode, Map<String, Object> setParams) {
        Map<String, Object> queryParams=new HashedMap();
        queryParams.put("targetCode",itemCode);
        super.updateOne(queryParams, setParams, collectionName);
    }

    @Override
    public void remove(String itemCode) {
        Map<String, Object> params=new HashedMap();
        params.put("targetCode",itemCode);
        super.remove(params, collectionName);
    }

    @Override
    public long countByParams(Map<String, Object> params) {
        return super.countByParams(params,collectionName);
    }

    private Query getCommonQuery(Target target){
        Query query = new Query();
        BasicDBObject query1 = new BasicDBObject();
        Map<String, Object> startT = new HashMap<>();
        startT.put("$gt", target.getTargetStartTime());
        query1.put("targetStartTime", startT);
        return new BasicQuery(query1);
        /*Criteria criteria = new Criteria();
        criteria.where("");
        if(StringUtils.isNotEmpty(target.getTargetCode())){
            criteria = criteria.and("quotaId").is(target.getTargetCode());
        }
        if(StringUtils.isNotEmpty(target.getTargetName())){
            criteria = criteria.and("targetName").regex(".*?\\" +target.getTargetName()+ ".*");
        }
        if(StringUtils.isNotEmpty(target.getUniqueCode())){
            criteria = criteria.and("uniqueCode").is(target.getUniqueCode());
        }
        if(target.getTargetStartTime()!=null){
            //criteria = criteria.and("targetStartTime").lte(DateUtil.dateToISODATEString(target.getTargetStartTime()));
            criteria = criteria.and("targetStartTime").lte(target.getTargetStartTime().getTime());
        }
        if(target.getTargetEndTime()!=null){
            //criteria = criteria.and("targetEndTime").gte(DateUtil.dateToISODATEString(target.getTargetEndTime()));
            criteria = criteria.and("targetEndTime").gte(target.getTargetEndTime().getTime());
        }
        query.addCriteria(criteria);
        return query;*/
    }
}
