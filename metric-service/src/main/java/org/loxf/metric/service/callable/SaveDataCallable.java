package org.loxf.metric.service.callable;

import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.dal.dao.interfaces.DataDao;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionValueDao;
import org.loxf.metric.service.base.SpringApplicationContextUtil;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by luohj on 2017/7/15.
 */
public class SaveDataCallable implements Callable {
    private List<Map> data;
    private Map<String, Set> dimData;
    private String collectionName;

    private DataDao dataDao;
    private QuotaDimensionValueDao quotaDimensionValueDao;

    public SaveDataCallable(List<Map> data, Map<String, Set> dimData, String collectionName){
        this.data = data;
        this.dimData = dimData;
        this.collectionName = collectionName;

        dataDao = (DataDao)SpringApplicationContextUtil.getBean(DataDao.class);
        quotaDimensionValueDao = (QuotaDimensionValueDao)SpringApplicationContextUtil.getBean(QuotaDimensionValueDao.class);
    }
    @Override
    public Object call() throws Exception {
        // 异步保存数据
        // 保存数据
        dataDao.saveData(data, collectionName);
        // 保存维度值
        Iterator ite = dimData.keySet().iterator();
        while (ite.hasNext()) {
            String key = ite.next().toString();
            String [] arr = key.split("-");
            quotaDimensionValueDao.saveDimensionValue(new Date(Long.valueOf(arr[1])), arr[0], dimData.get(key));
        }
        return new BaseResult();
    }
}
