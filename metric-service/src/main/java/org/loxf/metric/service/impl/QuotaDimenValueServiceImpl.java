package org.loxf.metric.service.impl;

import org.apache.log4j.Logger;
import org.loxf.metric.api.IQuotaDimenValueService;
import org.loxf.metric.common.dto.*;
import org.loxf.metric.base.utils.MapAndBeanTransUtils;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionValueDao;
import org.loxf.metric.dal.po.QuotaDimensionValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * Created by luohj on 2017/5/15.
 */
@Service("quotaDimenValueService")
public class QuotaDimenValueServiceImpl implements IQuotaDimenValueService {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private QuotaDimensionValueDao quotaDimensionValueDao;

    @Override
    public BaseResult<String> insertItem(QuotaDimensionValueDto obj) {
        QuotaDimensionValue quotaDimensionValue = new QuotaDimensionValue();
        BeanUtils.copyProperties(obj, quotaDimensionValue);
        quotaDimensionValue.setCreateUserName(obj.getHandleUserName());
        return new BaseResult<>(quotaDimensionValueDao.insert(quotaDimensionValue));
    }

    @Override
    public BaseResult<PageData> getPageList(QuotaDimensionValueDto obj){
        return null;
    }

    @Override
    public BaseResult<QuotaDimensionValueDto> queryItemByCode(String itemCode,String handleUserName) {
        QuotaDimensionValue qryParams = new QuotaDimensionValue();
        qryParams.setDimCode(itemCode);
        QuotaDimensionValue quotaDimensionValue = quotaDimensionValueDao.findOne(qryParams);
        QuotaDimensionValueDto quotaDimensionValueDto = new QuotaDimensionValueDto();
        BeanUtils.copyProperties(quotaDimensionValue, quotaDimensionValueDto);//前者赋值给后者
        return new BaseResult<>(quotaDimensionValueDto);
    }

    @Override
    public BaseResult<String> updateItem(QuotaDimensionValueDto obj) {
        String itemCode = obj.getDimValueCode();
        if (StringUtils.isEmpty(itemCode)) {
            return new BaseResult<>("quotaDimensionValueCode不能为空！");
        }
        Map quotaDimensionValueMap = MapAndBeanTransUtils.transBean2Map(obj);
        quotaDimensionValueDao.updateOne(itemCode, quotaDimensionValueMap);
        return new BaseResult<>();
    }

    @Override
    public BaseResult<String> delItemByCode(String itemCode,String handleUserName) {
        quotaDimensionValueDao.remove(itemCode);
        return new BaseResult<>();
    }

}
