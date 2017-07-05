package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.OperationDao;
import org.loxf.metric.dal.po.Operation;
import org.springframework.stereotype.Component;

/**
 * Created by hutingting on 2017/7/4.
 */
@Component("operationDaoImpl")
public class OperationDaoImpl extends MongoDaoBase<Operation> implements OperationDao{
}
