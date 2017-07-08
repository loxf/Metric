package org.loxf.metric.service;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.common.dto.QuotaDimensionDto;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionDao;
import org.loxf.metric.dal.po.QuotaDimension;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/7.
 */
@Component
public class QuotaDimensionManager {
    @Autowired
    private QuotaDimensionDao quotaDimensionDao;

    public String insert(QuotaDimensionDto quotaDimensionDto){
        QuotaDimension quotaDimension=new QuotaDimension();
        BeanUtils.copyProperties(quotaDimensionDto,quotaDimension);
        quotaDimension.setCreatedAt(new Date());
        quotaDimension.setUpdatedAt(new Date());
        return quotaDimensionDao.insert(quotaDimension);
    }

    public QuotaDimensionDto getQuotaDimensionDtoByParams(Map<String, Object> params){
        QuotaDimension quotaDimension=getQuotaDimensionByParams(params);
        QuotaDimensionDto quotaDimensionDto=new QuotaDimensionDto();
        BeanUtils.copyProperties(quotaDimension, quotaDimensionDto);
        return quotaDimensionDto;
    }

    public QuotaDimension getQuotaDimensionByParams(Map<String, Object> params){
        return quotaDimensionDao.findOne(params);
    }

    public void updateQuotaDimByDimCode(String dimCode,Map<String, Object> setParams) {
        Map<String, Object> qryParams=new HashedMap();
        qryParams.put("dimCode",dimCode);
        setParams.put("updatedAt",new Date());
        quotaDimensionDao.update(qryParams,setParams);
    }
    public void delQuotaDimByDimCode(String dimCode) {
        Map<String, Object> qryParams=new HashedMap();
        qryParams.put("dimCode",dimCode);
        quotaDimensionDao.remove(qryParams);
    }


}
