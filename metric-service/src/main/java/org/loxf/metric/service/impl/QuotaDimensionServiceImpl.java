package org.loxf.metric.service.impl;

import org.loxf.metric.api.IQuotaDimensionService;
import org.loxf.metric.base.utils.MapAndBeanTransUtils;
import org.loxf.metric.common.constants.PermissionType;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.Pager;
import org.loxf.metric.common.dto.QuotaDimensionDto;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionDao;
import org.loxf.metric.dal.po.QuotaDimension;
import org.loxf.metric.service.aop.CheckUser;
import org.loxf.metric.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by luohj on 2017/7/13.
 */
public class QuotaDimensionServiceImpl extends BaseService implements IQuotaDimensionService {
    private static Logger logger = LoggerFactory.getLogger(QuotaDimensionServiceImpl.class);
    @Autowired
    private QuotaDimensionDao quotaDimensionDao;
    @Override
    @CheckUser(value = PermissionType.LOGIN, nameParam = "{0}.handleUserName")
    public BaseResult<PageData> getPageList(QuotaDimensionDto obj) {
        Pager pager = obj.getPager();
        if (pager == null) {
            return new BaseResult<>(ResultCodeEnum.PARAM_LACK.getCode(), "分页信息为空");
        }
        Map<String, Object> params = MapAndBeanTransUtils.transBean2Map(obj);
        return new BaseResult<>(getPageResult(quotaDimensionDao, params, pager.getStart(), pager.getRownum()));
    }

    @Override
    @CheckUser(value = PermissionType.ROOT, nameParam = "{0}.handleUserName")
    public BaseResult<String> insertItem(QuotaDimensionDto obj) {
        // 校验dimCode和uniqueCode的联合唯一性
        String [] props = new String[]{"dimCode", "uniqueCode"};
        Map<String, Object> params = MapAndBeanTransUtils.transBean2Map(obj, Arrays.asList(props));
        QuotaDimension exists = quotaDimensionDao.findOne(params);
        if(exists!=null){
            return new BaseResult<>(ResultCodeEnum.DATA_EXIST.getCode(), "当前维度CODE已存在");
        }
        QuotaDimension quotaDimension = new QuotaDimension();
        BeanUtils.copyProperties(obj, quotaDimension);
        return new BaseResult(quotaDimensionDao.insert(quotaDimension));
    }

    @Override
    @CheckUser(nameParam = "{1}")
    public BaseResult<QuotaDimensionDto> queryItemByCode(String itemCode, String handleUserName) {
        return new BaseResult(ResultCodeEnum.NOT_SUPPORT.getCode(), "接口弃用");
    }

    @Override
    @CheckUser(value = PermissionType.ROOT, nameParam = "{0}.handleUserName")
    public BaseResult<String> updateItem(QuotaDimensionDto obj) {
        String [] props = new String[]{"dimCode", "uniqueCode"};
        Map<String, Object> params = MapAndBeanTransUtils.transBean2Map(obj, Arrays.asList(props));
        quotaDimensionDao.updateOne(params, obj.getDimName());
        return new BaseResult(obj.getDimCode());
    }

    @Override
    @CheckUser(value = PermissionType.ROOT, nameParam = "{1}")
    public BaseResult<String> delItemByCode(String itemCode, String handleUserName) {
        // 删除维度，如果维度未被指标关联，可以删除，否则不能删除。
        return new BaseResult(ResultCodeEnum.NOT_SUPPORT.getCode(), "接口弃用");
    }

    @Override
    @CheckUser(value = PermissionType.ROOT, nameParam = "{2}")
    public BaseResult<String> delItemByCode(String itemCode, String uniqueCode, String handleUserName) {
        // 删除维度，如果维度未被指标关联，可以删除，否则不能删除。
        quotaDimensionDao.remove(itemCode, uniqueCode);
        return new BaseResult(itemCode);
    }
}
