package org.loxf.metric.core.mongo;

import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/6/26.
 */
public interface MongoBase<T> {
    /**
     * 添加
     */
    public void insert(T object, String collectionName);

    /**
     * 根据条件查找一个
     */
    public T findOne(Map<String, Object> params, Class clazz, String collectionName);

    /**
     * 根据条件查找所有
     */
    public List<T> findAll(Map<String, Object> params, Class clazz, String collectionName);

    /**
     * 根据条件查找所有（分页）
     */
    public List<T> findByPager(Map<String, Object> params, int start, int end, Class clazz, String collectionName);

    /**
     * 修改
     */
    public void update(Map<String, Object> queryParams, Map<String, Object> setParams, String collectionName);

    /**
     * 修改
     */
    public void updateOne(Map<String, Object> queryParams, Map<String, Object> setParams, String collectionName);

    /**
     * 创建集合
     */
    public void createCollection(String collectionName);

    /**
     * 根据条件删除
     */
    public void remove(Map<String, Object> params, String collectionName);
}
