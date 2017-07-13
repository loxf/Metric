package org.loxf.metric.dal.dao.interfaces;

import org.loxf.metric.core.mongo.MongoDaoBase;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/7/13.
 */
public interface DataDao {
    public void saveData(List<Map> data, String collectionName);
    public void rmData(Date circleTime, String collectionName);
    public void dropData( String collectionName);
}
