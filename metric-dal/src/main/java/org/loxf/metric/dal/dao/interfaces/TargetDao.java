package org.loxf.metric.dal.dao.interfaces;

import org.loxf.metric.core.mongo.IBaseDao;
import org.loxf.metric.dal.po.Target;

import java.util.List;

public interface TargetDao extends IBaseDao<Target> {
    public List<Target> findAllByQuery(Target target) ;
}