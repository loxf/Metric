package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.TargetItemDao;
import org.loxf.metric.dal.po.TargetItem;
import org.springframework.stereotype.Component;

/**
 * Created by hutingting on 2017/7/4.
 */
@Component("targetItemDaoImpl")
public class TargetItemDaoImpl extends MongoDaoBase<TargetItem> implements TargetItemDao{
}