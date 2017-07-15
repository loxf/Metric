package org.loxf.metric.service.impl;


import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.api.IChartService;
import org.loxf.metric.base.ItemList.QuotaItem;
import org.loxf.metric.base.constants.StandardState;
import org.loxf.metric.base.constants.VisibleTypeEnum;
import org.loxf.metric.common.constants.*;
import org.loxf.metric.common.dto.Pager;
import org.loxf.metric.dal.dao.interfaces.ChartDao;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.dal.dao.interfaces.QuotaDao;
import org.loxf.metric.dal.po.Chart;
import org.apache.log4j.Logger;
import org.loxf.metric.dal.po.Quota;
import org.loxf.metric.service.aop.CheckUser;
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
    private ChartDao chartDao;

    @Autowired
    private QuotaDao quotaDao;

    @Override
    @CheckUser(value = PermissionType.ROOT, nameParam = "{0}.handleUserName")
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
        } else if (!obj.getVisibleType().equals(VisibleTypeEnum.SPECIFICRANGE.name())) {
            logger.info("入参visibleType不在可见范围之内,visibleType=" + obj.getVisibleType() + "操作用户为：" + obj.getCreateUserName());
            result.setCode(ResultCodeEnum.PARAM_ERROR.getCode());
            result.setMsg(ResultCodeEnum.PARAM_ERROR.getCodeMsg());
            return result;
        }
        if (!StringUtils.isEmpty(obj.getVisibleType()) && (obj.getVisibleList() == null || obj.getVisibleList().size() == 0)) {
            logger.info("可见范围中没有用户列表，" + obj.getVisibleType() + "操作用户为：" + obj.getCreateUserName());
            result.setCode(ResultCodeEnum.PARAM_ERROR.getCode());
            result.setMsg(ResultCodeEnum.PARAM_ERROR.getCodeMsg());
            return result;
        }
        boolean flag=true;
        Set<QuotaItem> quotaItemList=obj.getQuotaList();
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
    @CheckUser(nameParam = "{0}.handleUserName")
    public BaseResult<PageData> getPageList(ChartDto obj) {
        Pager pager=obj.getPager();
        BaseResult validPagerResult = super.validPager(obj.getPager());
        if (ResultCodeEnum.SUCCESS.getCode().equals(validPagerResult.getCode())) {
            Chart chart=new Chart();
            BeanUtils.copyProperties(obj, chart);
            PageData pageData=getPermissionPageResult(chart,obj.getHandleUserName(), pager.getStart(), pager.getRownum());
            validPagerResult.setData(pageData);
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
    @CheckUser(nameParam = "{1}")
    public BaseResult<ChartDto> queryItemByCode(String itemCode, String handleUserName) {//是否要查询已失效的图
        BaseResult<ChartDto> result = new BaseResult<>();
        if (StringUtils.isEmpty(itemCode)) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        Chart chart = new Chart();
        chart.setChartCode(itemCode);
        //获取该用户可见范围内的图
        Chart visibleChart = chartDao.findOne(chart, handleUserName);
        ChartDto chartDto = null;
        if (visibleChart != null) {
            chartDto = new ChartDto();
            BeanUtils.copyProperties(visibleChart, chartDto);
        }
        //前者赋值给后者
        result.setCode(ResultCodeEnum.DATA_NOT_EXIST.getCode());
        result.setMsg(ResultCodeEnum.DATA_NOT_EXIST.getCodeMsg());
        return result;
    }

    @Override
    @CheckUser(nameParam = "{0}.handleUserName")
    public BaseResult<String> updateItem(ChartDto obj) {
//        BaseResult<String> result = new BaseResult<>();
//        String itemCode = obj.getChartCode();
//        if (StringUtils.isEmpty(itemCode) || StringUtils.isEmpty(obj.getCreateUserName())) {
//            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
//            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
//            return result;
//        }
//        Map chartMap = MapAndBeanTransUtils.transBean2Map(obj);
//        chartDao.updateOne(itemCode, chartMap);
//        result.setCode(ResultCodeEnum.SUCCESS.getCode());
//        return result;
        return  new BaseResult<>();
    }

    @Override
    @CheckUser(value = PermissionType.ROOT,nameParam = "{1}")
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
