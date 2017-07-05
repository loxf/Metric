package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.SyncJobInstDao;
import org.loxf.metric.dal.po.SyncJobInst;
import org.springframework.stereotype.Component;

/**
 * Created by hutingting on 2017/7/4.
 */
@Component("syncJobInstDaoImpl")
public class SyncJobInstDaoImpl extends MongoDaoBase<SyncJobInst> implements SyncJobInstDao{
}
