package org.loxf.metric.dal.dao.impl;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.base.ItemList.TargetItem;
import org.loxf.metric.base.ItemList.VisibleItem;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.constants.VisibleTypeEnum;
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
    private static String target_prefix = "TG_";

    @Override
    public String insert(Target target) {
        String sid = IdGenerator.generate(target_prefix);
        target.setTargetCode(sid);
        target.setCreatedAt(new Date());
        target.setUpdatedAt(new Date());
        super.insert(target, collectionName);
        return  sid;
    }

    @Override
    public Target findOne(Target object) {
        Target target= super.findOne(getCommonQuery(object), collectionName);
        return target;
    }

    @Override
    public List<Target> findAll(Target object) {
        List<Target> targetList=super.findAll(getCommonQuery(object), collectionName);
        return targetList;
    }

    @Override
    public List<Target> findByPager(Target object, int start, int pageSize) {
        List<Target> targetList=super.findByPager(getCommonQuery(object), start, pageSize, collectionName);
        return targetList;
    }

    @Override
    public void update(Target object, Map<String, Object> setParams) {
        super.update(getCommonQuery(object), setParams, collectionName);
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
    public long countByParams(Target object) {
        return super.countByParams(getCommonQuery(object),collectionName);
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
        if(target.getVisibleType().equals(VisibleTypeEnum.SPECIFICRANGE.name()) &&
                CollectionUtils.isNotEmpty(target.getVisibleList())){
            BasicDBList includeUser = new BasicDBList();
            for(VisibleItem visibleItem : target.getVisibleList()){
                includeUser.add(visibleItem.getUserName());
            }
            query.put("visibleList.type", "user");// 目前只有用户
            query.put("visibleList.userName", new BasicDBObject("$in", includeUser));
            query.put("visibleType", VisibleTypeEnum.SPECIFICRANGE.name());
        }
        return new BasicQuery(query);
    }
}
