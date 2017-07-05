package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.QuotaScanDao;
import org.loxf.metric.dal.po.QuotaScan;
import org.springframework.stereotype.Component;

/**
 * Created by hutingting on 2017/7/4.
 */
@Component("quotaScanDaoImpl")
public class QuotaScanDaoImpl extends MongoDaoBase<QuotaScan> implements QuotaScanDao{
}
