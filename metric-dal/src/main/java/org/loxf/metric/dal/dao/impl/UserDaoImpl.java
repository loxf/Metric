package org.loxf.metric.dal.dao.impl;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.DateUtil;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.UserDao;
import org.loxf.metric.dal.po.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/5.
 */
@Service("user")
public class UserDaoImpl extends MongoDaoBase<User> implements UserDao {
    private final String collectionName = CollectionConstants.USER.getCollectionName();
    private static String target_prefix = "USER_";

    @Override
    public String insert(User object) {
        String sid = IdGenerator.generate(target_prefix);
        object.setUserCode(sid);
        object.handleDateToMongo();
        super.insert(object, collectionName);
        return  sid;
    }

    @Override
    public User findOne(Map<String, Object> params) {
        User user= super.findOne(params, collectionName);
        user.handleMongoDateToJava();
        return user;
    }

    private void handleDateForList(List<User> list){
        for(User user:list){
            user.handleMongoDateToJava();
        }
    }
    @Override
    public List<User> findAll(Map<String, Object> params) {
        List<User> userList=super.findAll(params, collectionName);
        handleDateForList(userList);
        return userList;
    }

    @Override
    public List<User> findByPager(Map<String, Object> params, int start, int pageSize) {
        List<User> userList=super.findByPager(params, start, pageSize, collectionName);
        handleDateForList(userList);
        return userList;
    }

    @Override
    public void update(Map<String, Object> queryParams, Map<String, Object> setParams) {
        super.update(queryParams, setParams, collectionName);
    }

    @Override
    public void updateOne(String itemCode, Map<String, Object> setParams) {
        Map<String, Object> queryParams=new HashedMap();
        queryParams.put("userCode",itemCode);
        super.updateOne(queryParams, setParams, collectionName);
    }

    @Override
    public void remove(String itemCode) {
        Map<String, Object> params=new HashedMap();
        params.put("userCode",itemCode);
        super.remove(params, collectionName);
    }


    @Override
    public long countByParams(Map<String, Object> params) {
        return super.countByParams(params,collectionName);
    }

}
