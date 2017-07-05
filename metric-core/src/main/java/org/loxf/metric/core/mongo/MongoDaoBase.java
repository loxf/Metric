package org.loxf.metric.core.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/6/27.
 */
public class MongoDaoBase<T> {
    private static final String SET = "$set";
    private static final String INC = "$inc";
    private static final String PUSH = "$push";
    @Resource
    protected MongoTemplate mongoTemplate;

    public void insert(T object, String collectionName) {
        mongoTemplate.insert(object, collectionName);
    }

    public T findOne(Map<String, Object> params, String collectionName) {
        DBObject obj = new BasicDBObject();
        obj.putAll(params);
        Query query = new BasicQuery(obj);
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return mongoTemplate.findOne(query, clazz, collectionName);
    }

    public List<T> findAll(Map<String, Object> params, String collectionName) {
        DBObject obj = new BasicDBObject();
        obj.putAll(params);
        Query query = new BasicQuery(obj);
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return mongoTemplate.find(query, clazz, collectionName);
    }

    public List<T> findByPager(Map<String, Object> params, int start, int end, String collectionName) {
        DBObject obj = new BasicDBObject();
        obj.putAll(params);
        Query query = new BasicQuery(obj);
        query.skip(start).limit(end);
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return mongoTemplate.find(query, clazz, collectionName);
    }

    public void update(Map<String, Object> queryParams, Map<String, Object> setParams, String collectionName) {
        DBObject queryObj = new BasicDBObject();
        queryObj.putAll(queryParams);
        Query query = new BasicQuery(queryObj);
        BasicDBObject setObj = new BasicDBObject();
        setObj.put(SET, new BasicDBObject(setParams));
        Update update = new BasicUpdate(setObj);
        mongoTemplate.updateMulti(query, update, collectionName);
    }

    public void updateOne(Map<String, Object> queryParams, Map<String, Object> setParams, String collectionName) {
        DBObject queryObj = new BasicDBObject();
        queryObj.putAll(queryParams);
        Query query = new BasicQuery(queryObj);
        BasicDBObject setObj = new BasicDBObject();
        setObj.put(SET, new BasicDBObject(setParams));
        Update update = new BasicUpdate(setObj);
        mongoTemplate.updateFirst(query, update, collectionName);
    }

    public void createCollection(String collectionName) {
        mongoTemplate.createCollection(collectionName);
    }

    public void remove(Map<String, Object> params, String collectionName) {
        DBObject obj = new BasicDBObject();
        obj.putAll(params);
        Query query = new BasicQuery(obj);
        mongoTemplate.remove(query, collectionName);
    }
}
