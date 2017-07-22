package org.loxf.metric.api;

import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;

import java.util.List;

/**
 * Created by hutingting on 2017/7/7.
 */
public interface IBaseService<T> {
    public BaseResult<PageData<T>> getPageList(T obj);
    public BaseResult<String> insertItem(T obj);
    public BaseResult<T> queryItemByCode(String itemCode, String handleUserName);
    public BaseResult<String> updateItem(T obj);
    public BaseResult<String> delItemByCode(String itemCode, String handleUserName);
}
