package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionDao;
import org.loxf.metric.dal.po.QuotaDimension;
import org.springframework.stereotype.Component;

/**
 * Created by hutingting on 2017/7/4.
 */
@Component("quotaDimensionImpl")
public class QuotaDimensionImpl extends MongoDaoBase<QuotaDimension> implements QuotaDimensionDao{
}
