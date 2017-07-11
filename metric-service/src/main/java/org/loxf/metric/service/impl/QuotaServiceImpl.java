package org.loxf.metric.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.api.IQuotaService;
import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.common.constants.QuotaType;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.utils.MapAndBeanTransUtils;
import org.loxf.metric.dal.dao.impl.QuotaDaoImpl;
import org.loxf.metric.dal.dao.interfaces.QuotaDao;
import org.loxf.metric.dal.po.Quota;
import org.loxf.metric.service.base.BaseService;
import org.loxf.metric.common.dto.*;
import org.loxf.metric.service.utils.QuotaSqlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuotaServiceImpl extends BaseService implements IQuotaService {
    private static Logger logger = LoggerFactory.getLogger(QuotaServiceImpl.class);

    @Autowired
    private QuotaDao quotaDao;

    @Override
    public BaseResult<String> insertItem(QuotaDto quotaDto) {
        BaseResult validResult = validQuota(quotaDto);
        if(ResultCodeEnum.SUCCESS.getCode().equals(validResult.getCode())){
            try {
                if(quotaDto.getType().equals(QuotaType.BASIC.getValue())){
                    // 基础指标
                    String ids = null;
                    while (true) {
                        ids = IdGenerator.generate("QUOTA_",13);
                        Map<String, Object> qryParams = new HashedMap();
                        qryParams.put("quotaCode", ids);
                        Quota quota = quotaDao.findOne(qryParams);
                        if(quota==null){
                            break;
                        }
                    }
                    quotaDto.setQuotaCode(ids);
                    quotaDto.setQuotaSource("q_d_" + ids.substring(6));
                } else if(quotaDto.getType().equals(QuotaType.COMPOSITE.getValue())) {
                    // 复合指标
                    // 获取表达式的源
                    String quotaSource = quotaDto.getQuotaSource();
                    // 复合指标复合复合指标时，需要把运算公式换算为基础指标
                    List<String> quotaCodeList = QuotaSqlBuilder.quotaList(quotaSource);
                    if(CollectionUtils.isEmpty(quotaCodeList)){
                        return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "复合指标必须基于其他指标运算");
                    }
                }
                Quota quota = new Quota();
                BeanUtils.copyProperties(quotaDto, quota);
                return new BaseResult(quotaDao.insert(quota));
            } catch (Exception e) {
                logger.error("创建指标失败", e);
                throw new MetricException("创建指标失败", e);
            }
        }
        return validResult;
    }

    private BaseResult validQuota(QuotaDto quotaDto){
        if(StringUtils.isEmpty(quotaDto.getQuotaName())){
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(),"指标名称为空");
        }
        if(StringUtils.isEmpty(quotaDto.getShowOperation())){
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(),"指标展现方式为空");
        }
        if(StringUtils.isEmpty(quotaDto.getShowType())){
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(),"指标展示类型为空");
        }
        if(StringUtils.isEmpty(quotaDto.getShowType())){
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(),"指标展示类型为空");
        }
        if(StringUtils.isEmpty(quotaDto.getShowType())){
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(),"指标展示类型为空");
        }
        if(quotaDto.getIntervalPeriod()==0){
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(),"指标统计周期为0");
        }
        if(StringUtils.isEmpty(quotaDto.getType())){
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(),"指标类型为空");
        } else if(quotaDto.getType().equals(QuotaType.BASIC.getValue())){
            if (StringUtils.isEmpty(quotaDto.getDataImportType())) {
                return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "指标数据接入方式为空");
            }
        } else {
            if(StringUtils.isEmpty(quotaDto.getQuotaSource())){
                return new BaseResult(ResultCodeEnum.PARAM_LACK.getCode(), "指标源不能为空");
            }
        }
        if(StringUtils.isEmpty(quotaDto.getCreateUserName())){
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(),"指标创建人为空");
        }
        return new BaseResult();
    }

    @Override
    public PageData getPageList(QuotaDto obj) {
        Pager pager=obj.getPager();
        if(pager==null){
            logger.info("分页信息为空，无法查询!");
            return null;
        }
        Map<String, Object> params= MapAndBeanTransUtils.transBean2Map(obj);
        return getPageResult(quotaDao, params, pager.getStart(), pager.getRownum());
    }

    @Override
    public BaseResult<QuotaDto> queryItemByCode(String itemCode) {
        Map<String, Object> qryParams=new HashedMap();
        qryParams.put("quotaCode",itemCode);
        Quota quota=quotaDao.findOne(qryParams);
        QuotaDto quotaDto=new QuotaDto();
        BeanUtils.copyProperties(quota,quotaDto);//前者赋值给后者
        return new BaseResult<>(quotaDto);
    }

    @Override
    public BaseResult<String> updateItem(QuotaDto obj) {
        String itemCode=obj.getQuotaCode();
        if(org.springframework.util.StringUtils.isEmpty(itemCode)){
            return new BaseResult<>("quotaCode不能为空！");
        }
        Map quotaMap= MapAndBeanTransUtils.transBean2Map(obj);
        quotaDao.updateOne(itemCode,quotaMap);
        return new BaseResult<>();
    }

    @Override
    public BaseResult<String> delItemByCode(String itemCode) {
        quotaDao.remove(itemCode);
        return new BaseResult<>();
    }

}
