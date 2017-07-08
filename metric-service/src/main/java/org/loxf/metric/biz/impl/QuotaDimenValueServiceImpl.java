package org.loxf.metric.biz.impl;

import org.loxf.metric.client.IQuotaDimenValueService;
import org.loxf.metric.client.QuotaDimenValueService;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.QuotaDimensionValueDto;
import org.loxf.metric.dal.po.QuotaDimensionValue;
import org.loxf.metric.service.QuotaDimensionValueManager;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/5/15.
 */
@Service("quotaDimenValueService")
public class QuotaDimenValueServiceImpl implements IQuotaDimenValueService {
    @Autowired
    private QuotaDimensionValueManager quotaDimensionValueManager;

    @Override
    public PageData getPageList(QuotaDimensionValueDto obj) {
        return null;
    }

    @Override
    public BaseResult<String> insertIterm(QuotaDimensionValueDto obj) {
        return null;
    }

    @Override
    public BaseResult<QuotaDimensionValueDto> queryItemByCode(String chartCode) {
        return null;
    }

    @Override
    public BaseResult<String> updateItem(String chartCode, Map<String, Object> setParams) {
        return null;
    }

    @Override
    public BaseResult<String> delItemByCode(String chartCode) {
        return null;
    }

//    public List<QuotaDimensionValueDto> queryValueByColumnCode(String columnCode){
//        List<QuotaDimensionValue> valueList= quotaDimensionValueManager.queryValueByColumnCode(columnCode);
//        if(CollectionUtils.isNotEmpty(valueList)){
//            List<QuotaDimensionValueDto> result = new ArrayList<>();
//            for(QuotaDimensionValue value:valueList){
//                QuotaDimensionValueDto dto = new QuotaDimensionValueDto();
//                BeanUtils.copyProperties(value, dto);
//                result.add(dto);
//            }
//            return result;
//        }
//        return null;
//    }
}
