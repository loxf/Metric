package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.UserQuotaScanRelDao;
import org.loxf.metric.dal.po.UserQuotaScanRel;
import org.springframework.stereotype.Component;

/**
 * Created by hutingting on 2017/7/4.
 */
@Component("userQuotaScanRelDaoImpl")
public class UserQuotaScanRelDaoImpl extends MongoDaoBase<UserQuotaScanRel> implements UserQuotaScanRelDao {
}
