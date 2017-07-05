package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.TargetNoticeUserDao;
import org.loxf.metric.dal.po.TargetNoticeUser;
import org.springframework.stereotype.Component;

/**
 * Created by hutingting on 2017/7/4.
 */
@Component("targetNoticeUserDaoImpl")
public class TargetNoticeUserDaoImpl extends MongoDaoBase<TargetNoticeUser> implements TargetNoticeUserDao {
}
