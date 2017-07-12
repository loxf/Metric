package org.loxf.metric.dal.dao.interfaces;

import org.loxf.metric.core.mongo.IBaseDao;
import org.loxf.metric.dal.po.Quota;

import java.util.List;

/**
 * Created by luohj on 2017/6/27.
 */
public interface QuotaDao extends IBaseDao<Quota> {
    public List<Quota> findAllByQuery(Quota target) ;
}
