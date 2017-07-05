package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.ChartQuotaRelDao;
import org.loxf.metric.dal.po.ChartQuotaRel;
import org.springframework.stereotype.Component;

/**
 * Created by hutingting on 2017/7/4.
 */
@Component("chartDaoImpl")
public class ChartQuotaRelImpl extends MongoDaoBase<ChartQuotaRel> implements ChartQuotaRelDao{
}
