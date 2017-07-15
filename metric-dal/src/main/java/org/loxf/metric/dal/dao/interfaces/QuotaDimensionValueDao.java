package org.loxf.metric.dal.dao.interfaces;

import org.loxf.metric.core.mongo.IBaseDao;
import org.loxf.metric.dal.po.QuotaDimensionValue;

import java.util.Date;
import java.util.Set;

public interface QuotaDimensionValueDao extends IBaseDao<QuotaDimensionValue> {
    void saveDimensionValue(Date circleTime, String key, Set<String> values);
}