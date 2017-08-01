package org.loxf.metric.service.impl;

import org.apache.commons.lang.StringUtils;
import org.loxf.metric.api.IIndexSettingService;
import org.loxf.metric.base.ItemList.ChartItem;
import org.loxf.metric.base.constants.VisibleTypeEnum;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.constants.UserTypeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.dal.dao.interfaces.ChartDao;
import org.loxf.metric.dal.dao.interfaces.IndexSettingDao;
import org.loxf.metric.dal.dao.interfaces.UserDao;
import org.loxf.metric.dal.po.Chart;
import org.loxf.metric.dal.po.IndexSetting;
import org.loxf.metric.dal.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    private ChartDao chartDao;

    @Override
    public BaseResult<String> addOrUpdateSetting(List<ChartItem> data, String handlerUserName, String uniqueCode) {
        indexSettingDao.addOrUpdateSetting(data, handlerUserName, uniqueCode);
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

    @Override
    public BaseResult<String> addChartList(String indexCode,UserDto handleUserDto, List<ChartItem> chartItemList) {
        if (StringUtils.isBlank(indexCode)||StringUtils.isBlank(handleUserDto.getUniqueCode())||StringUtils.isBlank(handleUserDto.getUserName())||chartItemList == null) {
            return new BaseResult<>(ResultCodeEnum.PARAM_LACK.getCode(),"首页配置码、团队码、操作人、待添加图列表信息缺失!");
        }

        List<String> addChartStrList=new ArrayList<>();
        boolean checkEmptyChartFlag = true;
        for (ChartItem chartItem : chartItemList) {
            if (StringUtils.isBlank(chartItem.getChartCode())|| StringUtils.isBlank(chartItem.getProperties())) {
                checkEmptyChartFlag = false;
                break;
            }
            addChartStrList.add(chartItem.getChartCode());
        }
        if (!checkEmptyChartFlag) {
            return new BaseResult<>(ResultCodeEnum.PARAM_LACK.getCode(),"图信息缺失,无法添加!");
        }

        IndexSetting indexSetting=indexSettingDao.getIndexSetting(indexCode);
        if(indexSetting==null){
            return new BaseResult<>(ResultCodeEnum.DATA_NOT_EXIST.getCode(),"查询不到首页看板!");
        }
        List<Chart> addChartList=chartDao.findAll(addChartStrList,handleUserDto.getUniqueCode());//添加图只能是root操作，不需考虑可见性。

        if (addChartList==null||(addChartList.size()!=chartItemList.size())) {
            return new BaseResult<>(ResultCodeEnum.DATA_NOT_EXIST.getCode(),"有图不存在,无法添加！");
        }
        List<ChartItem> indexChartItemList = indexSetting.getChartList();
        if(indexChartItemList==null){
            indexChartItemList=new ArrayList<>();
        }
        indexChartItemList.addAll(chartItemList);
        indexSettingDao.addOrUpdateSetting(indexChartItemList, handleUserDto.getUserName(),handleUserDto.getUniqueCode());
        return new BaseResult<>();
    }

    @Override
    public BaseResult<String> delChartList(String indexCode,UserDto handleUserDto,String chartCodeList) {
        if (StringUtils.isBlank(chartCodeList) || StringUtils.isBlank(indexCode)||StringUtils.isBlank(handleUserDto.getUniqueCode())||StringUtils.isBlank(handleUserDto.getUserName())) {
            return new BaseResult<>(ResultCodeEnum.PARAM_LACK.getCode(), "看板标志、图列表、操作人不能为空!");
        }
        IndexSetting indexSetting=indexSettingDao.getIndexSetting(indexCode);
        if(indexSetting==null){
            return new BaseResult<>(ResultCodeEnum.DATA_NOT_EXIST.getCode(),"查询不到首页看板!");
        }
        String[] chartCodeDelStr = chartCodeList.split(",");
        List<String> chartCodeDelList = Arrays.asList(chartCodeDelStr);
        List<ChartItem> chartItemList = indexSetting.getChartList();
        boolean flag = false;
        for (ChartItem chartItem : chartItemList) {
            String chartItemCode = chartItem.getChartCode();
            if (chartCodeDelList.contains(chartItemCode)) {
                chartItemList.remove(chartItem);
                flag = true;
            }
        }
        if (!flag) {
            return new BaseResult<>(ResultCodeEnum.DATA_NOT_EXIST.getCode(), "该看板没有配置需要删除的图!");
        }
        indexSettingDao.addOrUpdateSetting(chartItemList, handleUserDto.getUserName(),handleUserDto.getUniqueCode());
        return new BaseResult<>();

    }
}
