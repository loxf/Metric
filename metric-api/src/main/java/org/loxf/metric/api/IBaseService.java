package org.loxf.metric.api;

import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;

import java.util.Map;

/**
 * Created by hutingting on 2017/7/7.
 */
public interface IBaseService<T> {
    public PageData getPageList(T obj);
    public BaseResult<String> insertItem(T obj);
    public BaseResult<T> queryItemByCode(String itemCode);
    public BaseResult<String> updateItem(String itemCode,Map<String, Object> setParams);
    public BaseResult<String> delItemByCode(String itemCode);
}
