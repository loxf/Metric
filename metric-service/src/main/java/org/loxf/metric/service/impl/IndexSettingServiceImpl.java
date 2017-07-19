package org.loxf.metric.service.impl;

import org.loxf.metric.api.IIndexSettingService;
import org.loxf.metric.base.ItemList.ChartItem;
import org.loxf.metric.base.constants.VisibleTypeEnum;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.constants.UserTypeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.dal.dao.interfaces.IndexSettingDao;
import org.loxf.metric.dal.dao.interfaces.UserDao;
import org.loxf.metric.dal.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by luohj on 2017/7/19.
 */
@Service
public class IndexSettingServiceImpl implements IIndexSettingService {
    @Autowired
    private IndexSettingDao indexSettingDao;
    @Autowired
    private UserDao userDao;

    @Override
    public BaseResult<String> updateSetting(List<ChartItem> data, String handlerUserName, String uniqueCode) {
        indexSettingDao.updateSetting(data, handlerUserName, uniqueCode);
        return new BaseResult<>();
    }

    @Override
    public BaseResult<List<ChartItem>> getIndexSetting(String handlerUserName) {
        User user = new User();
        user.setUserName(handlerUserName);
        User existsUser = userDao.findOne(user);
        if (existsUser == null) {
            return new BaseResult(ResultCodeEnum.USER_NOT_EXIST.getCode(), ResultCodeEnum.USER_NOT_EXIST.getCodeMsg());
        }
        String visibleType = VisibleTypeEnum.ALL.name();
        if (existsUser.getUserType().equals(UserTypeEnum.CHILD.name())) {
            visibleType = VisibleTypeEnum.SPECIFICRANGE.name();
        }
        List<ChartItem> chartItemList = indexSettingDao.getIndexSetting(handlerUserName, visibleType, existsUser.getUniqueCode());
        return new BaseResult<>(chartItemList);
    }
}
