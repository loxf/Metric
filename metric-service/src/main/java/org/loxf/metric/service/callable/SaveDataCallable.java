package org.loxf.metric.service.callable;

import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.dal.dao.interfaces.DataDao;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionValueDao;
import org.loxf.metric.dal.po.QuotaDimensionValue;
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
    private String uniqueCode;

    private DataDao dataDao;
    private QuotaDimensionValueDao quotaDimensionValueDao;

    public SaveDataCallable(List<Map> data, Map<String, Set> dimData, String collectionName, String uniqueCode){
        this.data = data;
        this.dimData = dimData;
        this.collectionName = collectionName;
        this.uniqueCode = uniqueCode;

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
        List<QuotaDimensionValue> dimensionValueList = new ArrayList<>();
        while (ite.hasNext()) {
            String key = ite.next().toString();
            String [] arr = key.split("-");
            Set<String> values = dimData.get(key);
            for(String val : values) {
                QuotaDimensionValue dimValue = new QuotaDimensionValue();
                dimValue.setDimCode(arr[0]);
                dimValue.setDimName(arr[1]);
                dimValue.setDimValue(val);
                dimValue.setUniqueCode(uniqueCode);
                dimensionValueList.add(dimValue);
            }
        }
        quotaDimensionValueDao.saveDimensionValue(dimensionValueList);
        return new BaseResult();
    }
}
