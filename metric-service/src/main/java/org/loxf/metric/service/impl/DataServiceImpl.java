package org.loxf.metric.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.api.IDataService;
import org.loxf.metric.base.ItemList.QuotaDimItem;
import org.loxf.metric.base.utils.RandomUtils;
import org.loxf.metric.common.constants.QuotaType;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.QuotaDim;
import org.loxf.metric.common.dto.QuotaDto;
import org.loxf.metric.dal.dao.interfaces.DataDao;
import org.loxf.metric.dal.dao.interfaces.QuotaDao;
import org.loxf.metric.dal.po.Quota;
import org.loxf.metric.service.aop.CheckUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by luohj on 2017/7/13.
 */
@Service
public class DataServiceImpl implements IDataService {
    @Autowired
    private QuotaDao quotaDao;
    @Autowired
    private DataDao dataDao;
    @Override
    @CheckUser
    public BaseResult<QuotaDto> importData(String handleUserName, String quotaCode, List<Map> data) {
        if(CollectionUtils.isEmpty(data)){
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "导入数据为空");
        }
        BaseResult<QuotaDto> validQuota = validBasic(quotaCode);
        if(validQuota.getCode().equals(ResultCodeEnum.SUCCESS.getCode())) {
            QuotaDto dto = validQuota.getData();
            // 导入数据结构校验(抽样)
            validQuota = validDataStruct(data, dto);
            if(validQuota.getCode().equals(ResultCodeEnum.SUCCESS.getCode())) {
                dataDao.saveData(data, dto.getQuotaSource());
            }
        }
        return validQuota;
    }

    @Override
    @CheckUser
    public BaseResult<QuotaDto> rmDataByCircleTime(String handleUserName, String quotaCode, Date circleTime) {
        BaseResult<QuotaDto> validQuota = validBasic(quotaCode);
        if(validQuota.getCode().equals(ResultCodeEnum.SUCCESS.getCode())) {
            dataDao.rmData(circleTime, validQuota.getData().getQuotaSource());
        }
        return validQuota;
    }

    @Override
    @CheckUser
    public BaseResult<QuotaDto> dropAllData(String handleUserName, String quotaCode) {
        BaseResult<QuotaDto> validQuota = validBasic(quotaCode);
        if(validQuota.getCode().equals(ResultCodeEnum.SUCCESS.getCode())) {
            dataDao.dropData(validQuota.getData().getQuotaSource());
        }
        return validQuota;
    }

    private BaseResult<QuotaDto> validBasic(String quotaCode){
        Quota param = new Quota();
        param.setQuotaCode(quotaCode);
        Quota existsQuota = quotaDao.findOne(param);
        if(existsQuota!=null){
            if(!QuotaType.BASIC.getValue().equals(existsQuota.getType())){
                return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "数据导入/删除必须基于基础指标");
            }
            if(StringUtils.isEmpty(existsQuota.getQuotaSource())){
                return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "指标错误：源未指定");
            }
            QuotaDto dto = new QuotaDto();
            BeanUtils.copyProperties(existsQuota, dto);
            return new BaseResult(dto);
        } else {
            return new BaseResult(ResultCodeEnum.DATA_NOT_EXIST.getCode(), "指标不存在");
        }
    }

    private BaseResult validDataStruct(List<Map> data, QuotaDto quotaDto){
        List<QuotaDimItem> list = quotaDto.getQuotaDim();
        List<String> qimCodeArray = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(list)){
            for(QuotaDimItem item : list){
                qimCodeArray.add(item.getDimCode());
            }
        }
        int total = data.size();
        int checkTimes = total/3 + total%3;//随机校验次数 最少1/3的校验
        for(int i=0; i<checkTimes; i++){
            Map map = data.get(RandomUtils.getRandom(total));
            int countValueAndCircleTime = 0;
            Iterator ite = map.keySet().iterator();
            while (ite.hasNext()){
                String key = ite.next().toString();
                if("value".equals(key)||"circleTime".equals(key)){
                    countValueAndCircleTime++;
                    continue;
                } else {
                    if(!qimCodeArray.contains(key)){
                        return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "导入数据的结构错误：维度与定义不一致");
                    }
                }
            }
            if(countValueAndCircleTime!=2){
                return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "导入数据的结构错误：缺失值或账期");
            }
        }
        return new BaseResult();
    }

}
