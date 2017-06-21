package org.loxf.metric.service;

import org.loxf.metric.dal.dao.QuotaDimensionValueMapper;
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
    private QuotaDimensionValueMapper quotaDimensionValueMapper;

    public int exists(QuotaDimensionValue value){
        return quotaDimensionValueMapper.exists(value);
    }

    public int insert(QuotaDimensionValue value){
        return quotaDimensionValueMapper.insert(value);
    }

    public List<QuotaDimensionValue> queryValueByColumnCode(String columnCode){
        return quotaDimensionValueMapper.queryValueByColumnCode(columnCode);
    }
}
