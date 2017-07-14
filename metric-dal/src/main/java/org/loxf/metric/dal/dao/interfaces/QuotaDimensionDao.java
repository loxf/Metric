package org.loxf.metric.dal.dao.interfaces;
import org.loxf.metric.core.mongo.IBaseDao;
import org.loxf.metric.dal.po.QuotaDimension;

import java.util.Map;

public interface QuotaDimensionDao extends IBaseDao<QuotaDimension> {
    public QuotaDimension findOne(Map object) ;

    public void updateOne(Map params, String dimName) ;

    public void remove(String itemCode, String uniqueCode);
}