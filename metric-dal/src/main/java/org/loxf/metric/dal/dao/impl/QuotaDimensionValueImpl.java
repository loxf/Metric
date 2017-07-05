package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionValueDao;
import org.loxf.metric.dal.po.QuotaDimensionValue;
import org.springframework.stereotype.Component;

/**
 * Created by hutingting on 2017/7/4.
 */
@Component("quotaDimensionValueImpl")
public class QuotaDimensionValueImpl extends MongoDaoBase<QuotaDimensionValue> implements QuotaDimensionValueDao{
}
