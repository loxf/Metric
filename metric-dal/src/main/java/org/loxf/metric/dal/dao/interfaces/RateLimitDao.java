package org.loxf.metric.dal.dao.interfaces;

import org.loxf.metric.dal.po.RateLimit;

import java.util.List;

/**
 * Created by hutingting on 2017/7/22.
 */
public interface RateLimitDao {
    public List<RateLimit> getRateLimitList();
}
