package org.loxf.metric.dal.dao.interfaces;


import org.loxf.metric.core.mongo.IBaseDao;
import org.loxf.metric.dal.po.Chart;

import java.util.List;

public interface ChartDao extends IBaseDao<Chart> {
    public Chart findOne(Chart object,String handleUserName) ;

    public List<Chart> findAll(Chart object,String handleUserName) ;

    public List<Chart> findByPager(Chart object,String handleUserName, int start, int pageSize) ;

    public long countByParams(Chart object,String handleUserName);
}