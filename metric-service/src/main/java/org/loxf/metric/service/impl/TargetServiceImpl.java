package org.loxf.metric.service.impl;


import org.apache.commons.collections.CollectionUtils;
import org.loxf.metric.api.ITargetService;
import org.loxf.metric.base.ItemList.TargetItem;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.*;
import org.loxf.metric.base.utils.MapAndBeanTransUtils;
import org.loxf.metric.dal.dao.interfaces.TargetDao;
import org.loxf.metric.dal.po.Target;
import org.apache.log4j.Logger;
import org.loxf.metric.service.base.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 目标配置业务类
 * Created by caiyang on 2017/5/4.
 */
@Service("targetService")
public class TargetServiceImpl extends BaseService implements ITargetService {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private TargetDao targetDao;

    @Override
    public BaseResult<String> insertItem(TargetDto obj) {
        BaseResult validResult = validTarget(obj);
        if (validResult.getCode().equals(ResultCodeEnum.SUCCESS.getCode())) {
            Target target = new Target();
            BeanUtils.copyProperties(obj, target);
            target.setCreateUserName(obj.getHandleUserName());
            target.setUpdateUserName(obj.getHandleUserName());
            return new BaseResult<>(targetDao.insert(target));
        }
        return validResult;
    }

    private BaseResult<String> validTarget(TargetDto obj) {
        if (StringUtils.isEmpty(obj.getTargetName())) {
            return new BaseResult<>(ResultCodeEnum.PARAM_LACK.getCode(), "目标名为空");
        }
        if (obj.getTargetStartTime() == null || obj.getTargetEndTime() == null) {
            return new BaseResult<>(ResultCodeEnum.PARAM_LACK.getCode(), "目标起/始时间为空");
        } else {
            if (obj.getTargetEndTime().getTime() < obj.getTargetEndTime().getTime()) {
                return new BaseResult<>(ResultCodeEnum.PARAM_ERROR.getCode(), "目标开始时间大于结束时间");
            }
        }
        if(StringUtils.isEmpty(obj.getVisibleType())){
            return new BaseResult<>(ResultCodeEnum.PARAM_LACK.getCode(), "目标可见范围为空");
        }
        if (CollectionUtils.isEmpty(obj.getItemList())) {
            return new BaseResult<>(ResultCodeEnum.PARAM_LACK.getCode(), "目标的指标项为空");
        } else {
            int totalWeight = 0;
            for (TargetItem item : obj.getItemList()) {
                totalWeight += item.getWeight();
            }
            if (totalWeight != 100) {
                return new BaseResult<>(ResultCodeEnum.PARAM_ERROR.getCode(), "目标的所有指标的权重和不为100");
            }
        }
        return new BaseResult<>();
    }

    @Override
    public BaseResult<PageData> getPageList(TargetDto obj) {
        Pager pager = obj.getPager();
        BaseResult validPagerResult = super.validPager(obj.getPager());
        if (ResultCodeEnum.SUCCESS.getCode().equals(validPagerResult.getCode())) {
            Map<String, Object> params = MapAndBeanTransUtils.transBean2Map(obj);
            return new BaseResult<>(getPageResult(targetDao, params, pager.getStart(), pager.getRownum()));
        }
        return validPagerResult;
    }

    @Override
    public BaseResult<TargetDto> queryItemByCode(String itemCode, String handleUserName) {
        Target qryParams = new Target();
        qryParams.setTargetCode(itemCode);
        Target target = targetDao.findOne(qryParams);
        TargetDto targetDto = new TargetDto();
        BeanUtils.copyProperties(target, targetDto);//前者赋值给后者
        return new BaseResult<>(targetDto);
    }

    @Override
    public BaseResult<String> updateItem(TargetDto obj) {
        String itemCode = obj.getTargetCode();
        if (StringUtils.isEmpty(itemCode)) {
            return new BaseResult<>("targetCode为空");
        }
        Map targetMap = MapAndBeanTransUtils.transBean2Map(obj);
        targetDao.updateOne(itemCode, targetMap);
        return new BaseResult<>();
    }

    @Override
    public BaseResult<String> delItemByCode(String itemCode, String handleUserName) {
        targetDao.remove(itemCode);
        return new BaseResult<>();
    }

    @Override
    public BaseResult<List<TargetDto>> queryTarget(TargetDto targetDto) {
        Target target = new Target();
        BeanUtils.copyProperties(targetDto, target);
        return new BaseResult(targetDao.findAll(target));
    }
}
