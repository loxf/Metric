package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.TargetDao;
import org.loxf.metric.dal.po.Target;
import org.loxf.metric.dal.po.Target;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/4.
 */
@Service("targetDao")
public class TargetDaoImpl extends MongoDaoBase<Target> implements TargetDao{
    private final String collectionName = CollectionConstants.TARGET.name();
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
