package org.loxf.metric.biz.impl;

import org.loxf.metric.client.QuotaDimenValueService;
import org.loxf.metric.common.dto.QuotaDimensionValueDto;
import org.loxf.metric.dal.po.QuotaDimensionValue;
import org.loxf.metric.service.QuotaDimensionValueManager;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luohj on 2017/5/15.
 */
@Service("quotaDimenValueService")
public class QuotaDimenValueServiceImpl implements QuotaDimenValueService {
    @Autowired
    private QuotaDimensionValueManager quotaDimensionValueManager;

    public List<QuotaDimensionValueDto> queryValueByColumnCode(String columnCode){
        List<QuotaDimensionValue> valueList= quotaDimensionValueManager.queryValueByColumnCode(columnCode);
        if(CollectionUtils.isNotEmpty(valueList)){
            List<QuotaDimensionValueDto> result = new ArrayList<>();
            for(QuotaDimensionValue value:valueList){
                QuotaDimensionValueDto dto = new QuotaDimensionValueDto();
                BeanUtils.copyProperties(value, dto);
                result.add(dto);
            }
            return result;
        }
        return null;
    }
}
