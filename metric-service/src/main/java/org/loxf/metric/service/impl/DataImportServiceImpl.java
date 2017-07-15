package org.loxf.metric.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.api.IChartService;
import org.loxf.metric.api.IDataImportService;
import org.loxf.metric.base.ItemList.QuotaDimItem;
import org.loxf.metric.common.constants.PermissionType;
import org.loxf.metric.common.constants.QuotaType;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.*;
import org.loxf.metric.dal.dao.interfaces.DataDao;
import org.loxf.metric.dal.dao.interfaces.QuotaDao;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionValueDao;
import org.loxf.metric.dal.po.Quota;
import org.loxf.metric.permission.CheckUser;
import org.loxf.metric.service.callable.SaveDataCallable;
import org.loxf.metric.service.utils.PoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by luohj on 2017/7/13.
 */
@Service
public class DataImportServiceImpl implements IDataImportService {
    private static Logger logger = LoggerFactory.getLogger(DataImportServiceImpl.class);
    @Autowired
    private QuotaDao quotaDao;
    @Autowired
    private DataDao dataDao;
    @Autowired
    private IChartService chartService;
    @Autowired
    private QuotaDimensionValueDao quotaDimensionValueDao;

    @Override
    @CheckUser(PermissionType.ROOT)
    public BaseResult<QuotaDto> importData(String handleUserName, String quotaCode, List<Map> data) {
        if (CollectionUtils.isEmpty(data)) {
            return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "导入数据为空");
        }
        BaseResult<QuotaDto> validQuota = validBasic(quotaCode);
        if (validQuota.getCode().equals(ResultCodeEnum.SUCCESS.getCode())) {
            QuotaDto dto = validQuota.getData();
            // 导入数据结构校验
            BaseResult validDataStruct = validDataStruct(data, dto);
            if (validDataStruct.getCode().equals(ResultCodeEnum.SUCCESS.getCode())) {
                Map<String, Set> dimValueMap = (Map<String, Set>) validDataStruct.getData();
                SaveDataCallable callable = new SaveDataCallable(data, dimValueMap, dto.getQuotaSource());
                try {
                    PoolUtil.getPool().submit(callable);
                } catch (Exception e) {
                    logger.error("执行数据导入出错", e);
                }
            } else {
                validDataStruct.setData(dto);
                return validDataStruct;
            }
        }
        return validQuota;
    }

    @Override
    @CheckUser(PermissionType.ROOT)
    public BaseResult<QuotaDto> rmDataByCircleTime(String handleUserName, String quotaCode, Date circleTime) {
        BaseResult<QuotaDto> validQuota = validBasic(quotaCode);
        if (validQuota.getCode().equals(ResultCodeEnum.SUCCESS.getCode())) {
            dataDao.rmData(circleTime, validQuota.getData().getQuotaSource());
        }
        return validQuota;
    }

    @Override
    @CheckUser(PermissionType.ROOT)
    public BaseResult<QuotaDto> dropAllData(String handleUserName, String quotaCode) {
        BaseResult<QuotaDto> validQuota = validBasic(quotaCode);
        if (validQuota.getCode().equals(ResultCodeEnum.SUCCESS.getCode())) {
            dataDao.dropData(validQuota.getData().getQuotaSource());
        }
        return validQuota;
    }

    private BaseResult<QuotaDto> validBasic(String quotaCode) {
        Quota param = new Quota();
        param.setQuotaCode(quotaCode);
        Quota existsQuota = quotaDao.findOne(param);
        if (existsQuota != null) {
            if (!QuotaType.BASIC.getValue().equals(existsQuota.getType())) {
                return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "数据导入/删除必须基于基础指标");
            }
            if (StringUtils.isEmpty(existsQuota.getQuotaSource())) {
                return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "指标错误：源未指定");
            }
            QuotaDto dto = new QuotaDto();
            BeanUtils.copyProperties(existsQuota, dto);
            return new BaseResult(dto);
        } else {
            return new BaseResult(ResultCodeEnum.DATA_NOT_EXIST.getCode(), "指标不存在");
        }
    }

    private BaseResult validDataStruct(List<Map> data, QuotaDto quotaDto) {
        List<QuotaDimItem> list = quotaDto.getQuotaDim();
        List<String> qimCodeArray = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (QuotaDimItem item : list) {
                qimCodeArray.add(item.getDimCode());
            }
        }
        Map<String, Set> dimValueMap = new HashMap();
        for (int i = 0; i < data.size(); i++) {
            Map map = data.get(i);
            if (!map.containsKey("value") || !map.containsKey("circleTime")) {
                return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "导入数据的结构错误：缺失值或账期");
            }
            if (map.get("circleTime") == null || !(map.get("circleTime") instanceof Date)) {
                return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "导入数据错误：账期不正确");
            }
            long circleTime = ((Date) map.get("circleTime")).getTime();
            Iterator ite = map.keySet().iterator();
            while (ite.hasNext()) {
                String key = ite.next().toString();
                if (!"value".equals(key) && !"circleTime".equals(key)) {
                    if (!qimCodeArray.contains(key)) {
                        return new BaseResult(ResultCodeEnum.PARAM_ERROR.getCode(), "导入数据的结构错误：维度与定义不一致");
                    }
                    String dimKey = key + "-" + circleTime;
                    if (dimValueMap.containsKey(dimKey)) {
                        dimValueMap.get(dimKey).add(map.get(key) == null ? "" : map.get(key).toString());
                    } else {
                        Set<String> values = new HashSet<>();
                        values.add(map.get(key) == null ? "" : map.get(key).toString());
                        dimValueMap.put(dimKey, values);
                    }
                }
            }
        }
        return new BaseResult(dimValueMap);
    }

}
