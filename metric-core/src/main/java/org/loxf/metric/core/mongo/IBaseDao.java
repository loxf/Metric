package org.loxf.metric.core.mongo;

import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/5.
 */
public interface IBaseDao<T> {
    public String insert(T object) ;

    public T findOne(Map<String, Object> params) ;

    public List<T> findAll(Map<String, Object> params) ;

    public List<T> findByPager(Map<String, Object> params, int start, int pageSize) ;

    public long countByParams(Map<String, Object> params);

    public void update(Map<String, Object> queryParams, Map<String, Object> setParams) ;

    public void updateOne(String itemCode, Map<String, Object> setParams) ;

    public void remove(String itemCode);

}
