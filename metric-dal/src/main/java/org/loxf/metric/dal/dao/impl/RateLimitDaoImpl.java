package org.loxf.metric.dal.dao.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.mongodb.BasicDBObject;
import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.RateLimitDao;
import org.loxf.metric.dal.po.RateLimit;
import org.loxf.metric.dal.po.User;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.List;

/**
 * Created by hutingting on 2017/7/22.
 */
@Service("rateLimitDao")
public class RateLimitDaoImpl extends MongoDaoBase<RateLimit> implements RateLimitDao {
    private final String collectionName = CollectionConstants.RATE_LIMIT.getCollectionName();

    @Override
    public List<RateLimit> getRateLimitList() {
        return super.findAll(new HashedMap(),collectionName);
    }
}
