package org.loxf.metric.dal.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.base.ItemList.TargetItem;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.TargetDao;
import org.loxf.metric.dal.po.Target;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

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

    @Override
    public List<Target> findAllByQuery(Target target) {
        List<Target> targetList=super.findAll(getCommonQuery(target), collectionName);
        handleDateForList(targetList);
        return targetList;
    }

    private Query getCommonQuery(Target target){
        BasicDBObject query = new BasicDBObject();
		if(StringUtils.isNotEmpty(target.getTargetCode())){
            query.put("targetCode", target.getTargetCode());
        }
        if(StringUtils.isNotEmpty(target.getTargetName())){
            Pattern pattern = Pattern.compile("^.*" + target.getTargetName() +".*$", Pattern.CASE_INSENSITIVE);
            query.put("targetName", pattern);
        }
        if(StringUtils.isNotEmpty(target.getUniqueCode())){
            query.put("uniqueCode", target.getUniqueCode());
        }
        if(target.getTargetStartTime()!=null){
            Map<String, Object> startT = new HashMap<>();
            startT.put("$lt", target.getTargetStartTime());
            query.put("targetStartTime", startT);
        }
        if(target.getTargetEndTime()!=null){
            Map<String, Object> endT = new HashMap<>();
            endT.put("$gt", target.getTargetEndTime());
            query.put("targetEndTime", endT);
        }
        if(CollectionUtils.isNotEmpty(target.getItemList())){
            BasicDBList includeQuotaCodes = new BasicDBList();
            for(TargetItem item : target.getItemList()){
                includeQuotaCodes.add(item.getQuotaCode());
            }
            query.put("itemList.quotaCode", new BasicDBObject("$in", includeQuotaCodes));
        }
        return new BasicQuery(query);
    }
}
