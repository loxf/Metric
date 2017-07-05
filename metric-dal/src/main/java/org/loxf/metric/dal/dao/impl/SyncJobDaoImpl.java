package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.SyncJobDao;
import org.loxf.metric.dal.po.SyncJob;
import org.springframework.stereotype.Component;

/**
 * Created by hutingting on 2017/7/4.
 */
@Component("syncJobDaoImpl")
public class SyncJobDaoImpl extends MongoDaoBase<SyncJob> implements SyncJobDao{
}
