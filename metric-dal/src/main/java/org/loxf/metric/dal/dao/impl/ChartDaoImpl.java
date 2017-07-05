package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.ChartDao;
import org.loxf.metric.dal.po.Chart;
import org.springframework.stereotype.Component;

/**
 * Created by hutingting on 2017/7/4.
 */
@Component("chartDaoImpl")
public class ChartDaoImpl extends MongoDaoBase<Chart> implements ChartDao{
}
