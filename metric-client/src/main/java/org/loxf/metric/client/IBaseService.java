package org.loxf.metric.client;

import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;

import java.util.Map;

/**
 * Created by hutingting on 2017/7/7.
 */
public interface IBaseService<T> {
    //getPageList(T obj);insertIterm(T obj);queryItemByCode(String chartCode);
    //updateItem(String chartCode,Map<String, Object> setParams);
    //delItemByCode(String chartCode);
    public PageData getPageList(T obj);

    public BaseResult<String> insertIterm(T obj);

    public BaseResult<T> queryItemByCode(String itemCode);

    public BaseResult<String> updateItem(String itemCode,Map<String, Object> setParams);

    public BaseResult<String> delItemByCode(String itemCode);
}
