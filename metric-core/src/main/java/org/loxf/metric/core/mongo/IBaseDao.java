package org.loxf.metric.core.mongo;

import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/5.
 */
public interface IBaseDao<T> {
    public String insert(T object) ;

    public T findOne(T object) ;

    public List<T> findAll(T object) ;

    public List<T> findByPager(T object, int start, int pageSize) ;

    public long countByParams(T object);

    public void update(T object, Map<String, Object> setParams) ;

    public void updateOne(String itemCode, Map<String, Object> setParams) ;

    public void remove(String itemCode);

}
