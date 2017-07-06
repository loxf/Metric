package org.loxf.metric.service;

import org.loxf.metric.dal.dao.interfaces.QuotaDimensionDao;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionValueDao;
import org.loxf.metric.dal.po.QuotaDimension;
import org.loxf.metric.dal.po.QuotaDimensionValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by luohj on 2017/5/15.
 */
@Component
public class QuotaDimensionValueManager {
    @Autowired
    private QuotaDimensionValueDao quotaDimensionValueDao;

    public int exists(QuotaDimensionValue value){
        /*return quotaDimensionValueMapper.exists(value);*/
        return 0;
    }

    public int insert(QuotaDimensionValue value){
        quotaDimensionValueDao.insert(value);
        return 1;
    }

    public List<QuotaDimensionValue> queryValueByColumnCode(String columnCode){
        /*return quotaDimensionValueDao.queryValueByColumnCode(columnCode);*/
        return null;
    }
}
