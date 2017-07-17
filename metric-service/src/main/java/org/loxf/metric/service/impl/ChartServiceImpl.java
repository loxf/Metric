package org.loxf.metric.service.impl;


import com.sun.tools.javac.util.Assert;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.api.IChartService;
import org.loxf.metric.base.ItemList.QuotaItem;
import org.loxf.metric.base.constants.StandardState;
import org.loxf.metric.base.constants.VisibleTypeEnum;
import org.loxf.metric.base.utils.MapAndBeanTransUtils;
import org.loxf.metric.common.constants.*;
import org.loxf.metric.common.dto.Pager;
import org.loxf.metric.dal.dao.interfaces.ChartDao;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.dal.dao.interfaces.QuotaDao;
import org.loxf.metric.dal.dao.interfaces.UserDao;
import org.loxf.metric.dal.po.Chart;
import org.apache.log4j.Logger;
import org.loxf.metric.dal.po.Quota;
import org.loxf.metric.dal.po.User;
import org.loxf.metric.service.base.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 图配置业务类
 * Created by caiyang on 2017/5/4.
 */
@Service("chartService")
public class ChartServiceImpl extends BaseService implements IChartService {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private UserDao userDao;
    @Autowired
    private ChartDao chartDao;
    @Autowired
    private QuotaDao quotaDao;

    @Override
    public BaseResult<String> insertItem(ChartDto obj) {//前端去重，可见范围列表。只有root用户可以添加图
        BaseResult result = new BaseResult();
        if (StringUtils.isEmpty(obj.getChartName()) || StringUtils.isEmpty(obj.getType()) ||
                StringUtils.isEmpty(obj.getChartDim()) || obj.getQuotaList().size() == 0 ||
                obj.getQuotaList() == null||StringUtils.isEmpty(obj.getUniqueCode())||obj.getQuotaList()==null||obj.getQuotaList().size()==0) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg("图名称、类型、维度、指标、团队码都不能为空！操作用户为：" + obj.getCreateUserName());
            return result;
        }

        if (StringUtils.isEmpty(obj.getVisibleType())) {//全局可见，不传
            obj.setVisibleType(VisibleTypeEnum.ALL.name());
        } else if (!obj.getVisibleType().equals(VisibleTypeEnum.SPECIFICRANGE.name())
                && !obj.getVisibleType().equals(VisibleTypeEnum.ALL.name())) {
            logger.info("入参visibleType不在可见范围之内,visibleType=" + obj.getVisibleType() + "操作用户为：" + obj.getCreateUserName());
            result.setCode(ResultCodeEnum.PARAM_ERROR.getCode());
            result.setMsg(ResultCodeEnum.PARAM_ERROR.getCodeMsg());
            return result;
        }
        if (!StringUtils.isEmpty(obj.getVisibleType()) && (CollectionUtils.isEmpty(obj.getVisibleList()))) {
            logger.info("可见范围中没有用户列表，" + obj.getVisibleType() + "操作用户为：" + obj.getCreateUserName());
            result.setCode(ResultCodeEnum.PARAM_ERROR.getCode());
            result.setMsg(ResultCodeEnum.PARAM_ERROR.getCodeMsg());
            return result;
        }
        boolean flag=true;
        List<QuotaItem> quotaItemList=obj.getQuotaList();
        Quota baseQuota=new Quota();
        for(QuotaItem quotaItem:quotaItemList){
            baseQuota.setQuotaCode(quotaItem.getQuotaCode());
           if(quotaDao.findOne(baseQuota)==null){
               logger.info("不存在quotaCode=" + quotaItem.getQuotaCode() + "的指标");
               flag=false;
               break;
           }
        }
        if(!flag){
            result.setCode(ResultCodeEnum.DATA_NOT_EXIST.getCode());
            result.setMsg(ResultCodeEnum.DATA_NOT_EXIST.getCodeMsg());
            return result;
        }

        Chart chart = new Chart();
        BeanUtils.copyProperties(obj, chart);

        chart.setState(StandardState.AVAILABLE.getValue());
        chart.setCreatedAt(new Date());
        chart.setUpdatedAt(new Date());
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setData(chartDao.insert(chart));
        return result;
    }

    @Override
    public BaseResult<PageData> getPageList(ChartDto obj) {
        Pager pager=obj.getPager();
        BaseResult validPagerResult = super.validPager(obj.getPager());
        if (ResultCodeEnum.SUCCESS.getCode().equals(validPagerResult.getCode())) {
            Chart chart=new Chart();
            BeanUtils.copyProperties(obj, chart);
            User user = new User();
            user.setUserName(obj.getHandleUserName());
            user = userDao.findOne(user);
            if(user!=null) {
                if (UserTypeEnum.CHILD.name().equals(user.getUserType())) {
                    chart.setVisibleType(VisibleTypeEnum.SPECIFICRANGE.name());
                }
                validPagerResult.setData(getPermissionPageResult(chart,obj.getHandleUserName(), pager.getStart(), pager.getRownum()));
            }
        }
        return validPagerResult;
    }

    private PageData getPermissionPageResult(Chart obj, String handleUserName, int start, int pageSize) {
        try {
            long totalCount = (long) chartDao.countByParams(obj,handleUserName);
            if (totalCount <= 0) {
                return null;
            }
            List pageResult = chartDao.findByPager(obj,handleUserName, start, pageSize);
            PageData pageData=new PageData();
            pageData.setRows(pageResult);
            pageData.setTotalRecords(totalCount);
            pageData.setCurrentPage(start/pageSize+1);
            pageData.setRownum(pageSize);
            pageData.setTotalPage(pageData.calculateTotalPage());
            return pageData;
        }catch (Exception e){
            logger.error("查询分页异常！",e);
            return null;
        }
    }
    @Override
    public BaseResult<ChartDto> queryItemByCode(String itemCode, String handleUserName) {//是否要查询已失效的图
        BaseResult<ChartDto> result = new BaseResult<>();
        if (StringUtils.isEmpty(itemCode)) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        User user = new User();
        user.setUserName(handleUserName);
        user = userDao.findOne(user);
        if(user!=null) {
            Chart chart = new Chart();
            if(UserTypeEnum.CHILD.name().equals(user.getUserType())){
                chart.setVisibleType(VisibleTypeEnum.SPECIFICRANGE.name());
            }
            chart.setChartCode(itemCode);
            //获取该用户可见范围内的图
            Chart visibleChart = chartDao.findOne(chart, handleUserName);
            ChartDto chartDto = null;
            if (visibleChart != null) {
                chartDto = new ChartDto();
                BeanUtils.copyProperties(visibleChart, chartDto);
            }
            //前者赋值给后者
        } else {
            result.setCode(ResultCodeEnum.USER_NOT_EXIST.getCode());
            result.setMsg(ResultCodeEnum.USER_NOT_EXIST.getCodeMsg());
        }
        return result;
    }

    @Override
    public BaseResult<String> updateItem(ChartDto obj) {
        Assert.error("不支持图更新方法");
        return null;
    }

    @Override
    public BaseResult<String> delItemByCode(String itemCode, String handleUserName) {
        logger.info("客户:" + handleUserName + "将要删除图,itemCode" + itemCode);
        BaseResult<String> result = new BaseResult<>();
        if (StringUtils.isEmpty(itemCode) || StringUtils.isEmpty(handleUserName)) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        Map chartMap = new HashedMap();
        chartMap.put("state", StandardState.DISABLED.getValue());
        chartMap.put("updateUserName", handleUserName);
        chartDao.updateOne(itemCode, chartMap);
        chartDao.remove(itemCode);
        return result;
    }

}
