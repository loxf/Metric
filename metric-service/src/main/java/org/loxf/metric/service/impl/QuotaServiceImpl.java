package org.loxf.metric.service.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.api.IQuotaService;
import org.loxf.metric.base.ItemList.TargetItem;
import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.common.constants.QuotaType;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.base.utils.MapAndBeanTransUtils;
import org.loxf.metric.dal.dao.interfaces.QuotaDao;
import org.loxf.metric.dal.dao.interfaces.TargetDao;
import org.loxf.metric.dal.po.*;
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
    @Autowired
    private TargetDao targetDao;

    @Override
    public BaseResult<String> insertItem(QuotaDto quotaDto) {
        BaseResult result = validHandlerUser(quotaDto.getHandleUserName(), true);
        if(result.getCode().equals(ResultCodeEnum.SUCCESS)) {
            quotaDto.setCreateUserName(quotaDto.getHandleUserName());
            quotaDto.setUpdateUserName(quotaDto.getHandleUserName());
            result = validQuota(quotaDto);
            if (ResultCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
                try {
                    if (quotaDto.getType().equals(QuotaType.BASIC.getValue())) {
                        // 基础指标
                        String ids = null;
                        while (true) {
                            ids = IdGenerator.generate("QUOTA_", 13);
                            Quota qryParams = new Quota();
                            qryParams.setQuotaCode(ids);
                            Quota quota = quotaDao.findOne(qryParams);
                            if (quota == null) {
                                break;
                            }
                        }
                        quotaDto.setQuotaCode(ids);
                        quotaDto.setQuotaSource("q_d_" + ids.substring(6));
                    } else if (quotaDto.getType().equals(QuotaType.COMPOSITE.getValue())) {
                        // 复合指标
                        // 获取表达式的源
                        String quotaSource = quotaDto.getQuotaSource();
                        List<String> quotaCodeList = QuotaSqlBuilder.quotaList(quotaSource);
                        if (CollectionUtils.isEmpty(quotaCodeList)) {
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
        }
        return result;
    }

    private BaseResult validQuota(QuotaDto quotaDto) {
        if (StringUtils.isEmpty(quotaDto.getQuotaName())) {
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "指标名称为空");
        }
        if (StringUtils.isEmpty(quotaDto.getShowOperation())) {
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "指标展现方式为空");
        }
        if (StringUtils.isEmpty(quotaDto.getShowType())) {
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "指标展示类型为空");
        }
        if (StringUtils.isEmpty(quotaDto.getShowType())) {
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "指标展示类型为空");
        }
        if (StringUtils.isEmpty(quotaDto.getShowType())) {
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "指标展示类型为空");
        }
        if (quotaDto.getIntervalPeriod() == 0) {
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "指标统计周期为0");
        }
        if (StringUtils.isEmpty(quotaDto.getType())) {
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "指标类型为空");
        } else if (quotaDto.getType().equals(QuotaType.BASIC.getValue())) {
            if (StringUtils.isEmpty(quotaDto.getDataImportType())) {
                return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "指标数据接入方式为空");
            }
        } else {
            if (StringUtils.isEmpty(quotaDto.getQuotaSource())) {
                return new BaseResult(ResultCodeEnum.PARAM_LACK.getCode(), "指标源不能为空");
            }
        }
        return new BaseResult();
    }

    @Override
    public BaseResult<PageData> getPageList(QuotaDto obj) {
        BaseResult result = validHandlerUser(obj.getHandleUserName(), true);
        if(result.getCode().equals(ResultCodeEnum.SUCCESS)) {
            Pager pager = obj.getPager();
            if (pager == null) {
                logger.info("分页信息为空，无法查询!");
                return null;
            }
            Map<String, Object> params = MapAndBeanTransUtils.transBean2Map(obj);
            return new BaseResult<>(getPageResult(quotaDao, params, pager.getStart(), pager.getRownum()));
        }
        return result;
    }

    @Override
    public BaseResult<QuotaDto> queryItemByCode(String itemCode, String handleUserName) {
        BaseResult result = validHandlerUser(handleUserName, true);
        if(result.getCode().equals(ResultCodeEnum.SUCCESS)) {
            Quota qryParams = new Quota();
            qryParams.setQuotaCode(itemCode);
            Quota quota = quotaDao.findOne(qryParams);
            QuotaDto quotaDto = new QuotaDto();
            BeanUtils.copyProperties(quota, quotaDto);//前者赋值给后者
            return new BaseResult<>(quotaDto);
        }
        return result;
    }

    @Override
    public BaseResult<String> updateItem(QuotaDto obj) {
        BaseResult result = validHandlerUser(obj.getHandleUserName(), true);
        if(result.getCode().equals(ResultCodeEnum.SUCCESS)) {
            String itemCode = obj.getQuotaCode();
            if (StringUtils.isEmpty(itemCode)) {
                return new BaseResult<>("指标code不能为空！");
            }
            if (StringUtils.isEmpty(obj.getType())) {
                return new BaseResult<>("指标类型不能为空！");
            }
            Map quotaMap = null;
            if (obj.getType().equals(QuotaType.BASIC.getValue())) {
                // 展示类型（数量/金额/比例），展现方式（合计/最大值/最小值/平均值/计数），数据接入方式（SDK/EXCEL）
                String[] props = {"showOperation", "showType", "dataImportType"};
                quotaMap = MapAndBeanTransUtils.transBean2Map(obj, Arrays.asList(props));
            } else {
                quotaMap = MapAndBeanTransUtils.transBean2Map(obj);
            }
            quotaDao.updateOne(itemCode, quotaMap);
            return new BaseResult<>();
        }
        return result;
    }

    @Override
    public BaseResult<String> delItemByCode(String itemCode, String handleUserName) {
        BaseResult result = validHandlerUser(handleUserName, true);
        if(result.getCode().equals(ResultCodeEnum.SUCCESS)) {
            BaseResult<String> checkResult = checkDependencyQuota(itemCode);
            if (checkResult.getCode().equals(ResultCodeEnum.SUCCESS.getCode())) {
                quotaDao.remove(itemCode);
                return new BaseResult<>();
            }
            return checkResult;
        }
        return result;
    }

    @Override
    public BaseResult<String> checkDependencyQuota(String quotaCode){
        // 如果基础指标被其他指标引用，不能删除。如果被有效目标（当前日期在目标的时间范围）引用，不能删除。
        boolean flag = false;
        // 查询是否被指标依赖
        String[] quotaName = null;
        String quotaExpression = QuotaSqlBuilder.getQuotaExpress(quotaCode);
        Quota quotaMap = new Quota();
        quotaMap.setQuotaSource(quotaExpression);
        List<Quota> allDependencyByQuota = quotaDao.findAll(quotaMap);
        if(CollectionUtils.isNotEmpty(allDependencyByQuota)){
            flag = true;
            quotaName = new String[allDependencyByQuota.size()];
            int i=0;
            for(Quota q : allDependencyByQuota){
                quotaName[i] = q.getQuotaName();
            }
        }
        // 查询是否被目标依赖
        String[] targetName = null;
        Target target = new Target();
        TargetItem targetItem = new TargetItem();
        targetItem.setQuotaCode(quotaCode);
        List<TargetItem> itemList = new ArrayList<>();
        itemList.add(targetItem);
        target.setItemList(itemList);
        Date now = new Date();
        target.setTargetStartTime(now);
        target.setTargetEndTime(now);
        List<Target> targetList = targetDao.findAllByQuery(target);
        if(CollectionUtils.isNotEmpty(targetList)){
            flag = true;
            int i=0;
            targetName = new String[targetList.size()];
            for(Target t : targetList){
                targetName[i] = t.getTargetName();
            }

        }
        if(flag) {
            Map map = new HashMap();
            map.put("TARGET", targetName);
            map.put("QUOTA", quotaName);
            return new BaseResult<>(ResultCodeEnum.PARAM_ERROR.getCode(), "当前指标被依赖",
                    JSON.toJSONString(map));
        }
        return new BaseResult<>();
    }

    @Override
    public BaseResult<List<QuotaDto>> queryQuotaList(QuotaDto quotaDto) {
        Quota quota = new Quota();
        BeanUtils.copyProperties(quotaDto, quota);
        return new BaseResult(quotaDao.findAll(quota));
    }

}
