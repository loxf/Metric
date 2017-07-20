package org.loxf.metric.service.impl;

import org.loxf.metric.api.IQuotaDimensionService;
import org.loxf.metric.base.utils.MapAndBeanTransUtils;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.Pager;
import org.loxf.metric.common.dto.QuotaDimensionDto;
import org.loxf.metric.dal.dao.interfaces.QuotaDimensionDao;
import org.loxf.metric.dal.po.Quota;
import org.loxf.metric.dal.po.QuotaDimension;
import org.loxf.metric.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by luohj on 2017/7/13.
 */
@Service
public class QuotaDimensionServiceImpl extends BaseService implements IQuotaDimensionService {
    private static Logger logger = LoggerFactory.getLogger(QuotaDimensionServiceImpl.class);
    @Autowired
    private QuotaDimensionDao quotaDimensionDao;
    @Override
    public BaseResult<PageData<QuotaDimensionDto>> getPageList(QuotaDimensionDto obj) {
        Pager pager = obj.getPager();
        if (pager == null) {
            return new BaseResult<>(ResultCodeEnum.PARAM_LACK.getCode(), "分页信息为空");
        }
        Map<String, Object> params = MapAndBeanTransUtils.transBean2Map(obj);
        return new BaseResult<>(getPageResult(quotaDimensionDao, params, pager.getStart(), pager.getRownum()));
    }

    @Override
    public BaseResult<String> insertItem(QuotaDimensionDto obj) {
        QuotaDimension quotaDimension = new QuotaDimension();
        BeanUtils.copyProperties(obj, quotaDimension);
        quotaDimension.setCreateUserName(obj.getHandleUserName());
        quotaDimension.setUpdateUserName(obj.getHandleUserName());
        return new BaseResult(quotaDimensionDao.insert(quotaDimension));
    }

    @Override
    public BaseResult<QuotaDimensionDto> queryItemByCode(String itemCode, String handleUserName) {
        return new BaseResult(ResultCodeEnum.NOT_SUPPORT.getCode(), "接口弃用");
    }

    @Override
    public BaseResult<String> updateItem(QuotaDimensionDto obj) {
        String [] props = new String[]{"dimCode", "uniqueCode"};
        Map<String, Object> params = MapAndBeanTransUtils.transBean2Map(obj, Arrays.asList(props));
        quotaDimensionDao.updateOne(params, obj.getDimName());
        return new BaseResult(obj.getDimCode());
    }

    @Override
    public BaseResult<String> delItemByCode(String itemCode, String handleUserName) {
        // 删除维度，如果维度未被指标关联，可以删除，否则不能删除。
        return new BaseResult(ResultCodeEnum.NOT_SUPPORT.getCode(), "接口弃用");
    }

    @Override
    public BaseResult<String> delItemByCode(String itemCode, String uniqueCode, String handleUserName) {
        // 删除维度，如果维度未被指标关联，可以删除，否则不能删除。
        quotaDimensionDao.remove(itemCode, uniqueCode);
        return new BaseResult(itemCode);
    }
}
