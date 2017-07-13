package org.loxf.metric.dal.dao.impl;

import com.mongodb.BasicDBObject;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.DataDao;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/7/13.
 */
@Service("dataDao")
public class DataDaoImpl extends MongoDaoBase implements DataDao {
    @Override
    public void saveData(List<Map> data, String collectionName) {
        mongoTemplate.insert(data, collectionName);
    }

    @Override
    public void rmData(Date circleTime, String collectionName) {
        BasicDBObject query = new BasicDBObject();
        query.put("circleTime", circleTime);
        mongoTemplate.remove(new BasicQuery(query), collectionName);
    }

    @Override
    public void dropData(String collectionName) {
        mongoTemplate.dropCollection(collectionName);
    }
}
