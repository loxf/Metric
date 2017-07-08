package org.loxf.metric.service;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.common.dto.QuotaDimensionValueDto;
import org.loxf.metric.common.dto.QuotaDimensionValueDto;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionDao;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionValueDao;
import org.loxf.metric.dal.po.Chart;
import org.loxf.metric.dal.po.QuotaDimension;
import org.loxf.metric.dal.po.QuotaDimensionValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/5/15.
 */
@Component
public class QuotaDimensionValueManager {

    @Autowired
    private QuotaDimensionValueDao quotaDimensionValueDao;

    public String insert(QuotaDimensionValueDto quotaDimensionValueDto){
        QuotaDimensionValue quotaDimensionValue=new QuotaDimensionValue();
        BeanUtils.copyProperties(quotaDimensionValueDto,quotaDimensionValue);
        quotaDimensionValue.setCreatedAt(new Date());
        quotaDimensionValue.setUpdatedAt(new Date());
        return quotaDimensionValueDao.insert(quotaDimensionValue);
    }

    public QuotaDimensionValueDto getQuotaDimValueDtoByParams(Map<String, Object> params){
        QuotaDimensionValue quotaDimensionValue=getQuotaDimValueByParams(params);
        QuotaDimensionValueDto quotaDimensionValueDto=new QuotaDimensionValueDto();
        BeanUtils.copyProperties(quotaDimensionValue, quotaDimensionValueDto);
        return quotaDimensionValueDto;
    }

    public QuotaDimensionValue getQuotaDimValueByParams(Map<String, Object> params){
        return quotaDimensionValueDao.findOne(params);
    }

    public void updateQuotaDimValueByCode(String dimValueCode,Map<String, Object> setParams) {
        Map<String, Object> qryParams=new HashedMap();
        qryParams.put("dimValueCode",dimValueCode);
        setParams.put("updatedAt",new Date());
        quotaDimensionValueDao.update(qryParams,setParams);
    }
    public void delQuotaDimValueByCode(String dimValueCode) {
        Map<String, Object> qryParams=new HashedMap();
        qryParams.put("dimValueCode",dimValueCode);
        quotaDimensionValueDao.remove(qryParams);
    }








//
//
//    public int exists(QuotaDimensionValue value){
//        /*return quotaDimensionValueMapper.exists(value);*/
//        return 0;
//    }
//
//    public int insert(QuotaDimensionValue value){
//        quotaDimensionValueDao.insert(value);
//        return 1;
//    }
//
//    public List<QuotaDimensionValue> queryValueByColumnCode(String columnCode){
//        /*return quotaDimensionValueDao.queryValueByColumnCode(columnCode);*/
//        return null;
//    }
}
