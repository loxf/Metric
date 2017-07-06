package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.QuotaDataDao;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/7/5.
 */
@Resource
public class QuotaDataDaoImpl extends MongoDaoBase implements QuotaDataDao {
    /*public List<Map> queryByParams(String collection, Map queryParam, Map groupBy){
        return mongoTemplate.find(queryParam, collection);
    }*/
}
