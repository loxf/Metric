package org.loxf.metric.service.impl;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.api.ITargetService;
import org.loxf.metric.base.ItemList.TargetItem;
import org.loxf.metric.base.ItemList.VisibleItem;
import org.loxf.metric.base.constants.VisibleTypeEnum;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.constants.UserTypeEnum;
import org.loxf.metric.common.dto.*;
import org.loxf.metric.base.utils.MapAndBeanTransUtils;
import org.loxf.metric.dal.dao.interfaces.TargetDao;
import org.loxf.metric.dal.dao.interfaces.UserDao;
import org.loxf.metric.dal.po.Target;
import org.apache.log4j.Logger;
import org.loxf.metric.dal.po.User;
import org.loxf.metric.service.base.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private UserDao userDao;

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
        if (StringUtils.isEmpty(obj.getVisibleType())) {
            obj.setVisibleType(VisibleTypeEnum.ALL.name());
        }
        if (CollectionUtils.isEmpty(obj.getItemList())) {
            return new BaseResult<>(ResultCodeEnum.PARAM_LACK.getCode(), "目标的指标项为空");
        } else {
            int totalWeight = 0;
            for (TargetItem item : obj.getItemList()) {
                if (StringUtils.isBlank(item.getQuotaCode()) || StringUtils.isBlank(item.getTargetValue())) {
                    return new BaseResult<>(ResultCodeEnum.PARAM_ERROR.getCode(), "指标码和目标值必填!");
                }
                totalWeight += item.getWeight();
            }
            if (totalWeight != 100) {
                return new BaseResult<>(ResultCodeEnum.PARAM_ERROR.getCode(), "目标的所有指标的权重和不为100");
            }

        }
        return new BaseResult<>();
    }

    @Override
    public BaseResult<PageData<TargetDto>> getPageList(TargetDto obj) {
        Pager pager = obj.getPager();
        BaseResult validPagerResult = super.validPager(obj.getPager());
        if (ResultCodeEnum.SUCCESS.getCode().equals(validPagerResult.getCode())) {
            User user = new User();
            user.setUserName(obj.getHandleUserName());
            user = userDao.findOne(user);
            if (UserTypeEnum.CHILD.name().equals(user.getUserType())) {
                obj.setVisibleType(VisibleTypeEnum.SPECIFICRANGE.name());
                VisibleItem visibleItem = new VisibleItem();
                visibleItem.setUserName(obj.getHandleUserName());
                List<VisibleItem> list = new ArrayList<>();
                list.add(visibleItem);
                obj.setVisibleList(list);
            }else{
                obj.setVisibleType(VisibleTypeEnum.ALL.name());
            }
            Target target=new Target();
            BeanUtils.copyProperties(obj,target);
            return new BaseResult<>(getPageResult(targetDao, target, pager.getStart(), pager.getRownum()));
        }
        return validPagerResult;
    }

    @Override
    public BaseResult<TargetDto> queryItemByCode(String itemCode,UserDto userDto) {
        BaseResult result = new BaseResult();
        Target qryParams = new Target();
        if (UserTypeEnum.CHILD.name().equals(userDto.getUserType())) {
            qryParams.setVisibleType(VisibleTypeEnum.SPECIFICRANGE.name());
            VisibleItem visibleItem = new VisibleItem();
            visibleItem.setUserName(userDto.getUserName());
            List<VisibleItem> list = new ArrayList<>();
            list.add(visibleItem);
            qryParams.setVisibleList(list);
        }else{
            qryParams.setVisibleType(VisibleTypeEnum.ALL.name());
        }
        //获取该用户可见范围内的图
        qryParams.setTargetCode(itemCode);
        Target target = targetDao.findOne(qryParams);
        if (target != null) {
            TargetDto targetDto = new TargetDto();
            BeanUtils.copyProperties(target, targetDto);
            result.setData(targetDto);
        }
        return result;
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
