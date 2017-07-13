package org.loxf.metric.dal.dao.impl;

import com.mongodb.BasicDBObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.UserDao;
import org.loxf.metric.dal.po.User;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
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
        super.insert(object, collectionName);
        return  sid;
    }

    @Override
    public User findOne(User object) {
        User user= super.findOne(getCommonQuery(object), collectionName);
        return user;
    }

    @Override
    public List<User> findAll(User object) {
        List<User> userList=super.findAll(getCommonQuery(object), collectionName);
        return userList;
    }

    @Override
    public List<User> findByPager(User object, int start, int pageSize) {
        List<User> userList=super.findByPager(getCommonQuery(object), start, pageSize, collectionName);
        return userList;
    }

    @Override
    public void update(User object, Map<String, Object> setParams) {
        super.update(getCommonQuery(object), setParams, collectionName);
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
    public long countByParams(User object) {
        return super.countByParams(getCommonQuery(object),collectionName);
    }

    private Query getCommonQuery(User user){
        BasicDBObject query = new BasicDBObject();
        if(StringUtils.isNotEmpty(user.getUserName())){
            query.put("userName", user.getUserName());
        }
        // todo 未完
        return new BasicQuery(query);
    }
}
