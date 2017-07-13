package org.loxf.metric.service.impl;


import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.api.IChartService;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.constants.StandardState;
import org.loxf.metric.common.constants.UserTypeEnum;
import org.loxf.metric.common.constants.VisibleTypeEnum;
import org.loxf.metric.common.dto.Pager;
import org.loxf.metric.base.utils.MapAndBeanTransUtils;
import org.loxf.metric.dal.dao.interfaces.ChartDao;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.dal.dao.interfaces.UserDao;
import org.loxf.metric.dal.po.Chart;
import org.apache.log4j.Logger;
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
    private ChartDao chartDao;
    @Autowired
    private UserDao userDao;

    @Override
    public BaseResult<String> insertItem(ChartDto obj) {//前端去重，可见范围列表。只有root用户可以添加图
        BaseResult result = validHandlerUser(obj.getHandleUserName(), true);
        if(result.getCode().equals(ResultCodeEnum.SUCCESS)) {
            if (StringUtils.isEmpty(obj.getChartName()) || StringUtils.isEmpty(obj.getType()) ||
                    StringUtils.isEmpty(obj.getChartDim()) || obj.getQuotaList().size() == 0 || obj.getQuotaList() == null) {
                result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
                result.setMsg("图名称、类型、维度、指标都不能为空！操作用户为：" + obj.getCreateUserName());
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
            Chart chart = new Chart();
            BeanUtils.copyProperties(obj, chart);
            //是否需要校验可见范围内的用户合法性。
//        Set<String> visibleSet=obj.getVisibleList();//入参指定用户
//        StringBuilder notExistList=null;
//        Set<String> findUserSet= findAllUserName(visibleSet);//查询出来的用户
//        for(String userName:visibleSet){
//            if(!(findUserSet.contains(userName))){
//                notExistList.append("userName,");
//            }
//        }
//        if(notExistList!=null){
//            result.setCode(ResultCodeEnum.DATA_NOT_EXIST.getCode());
//            result.setMsg("数据库中不存在以下用户："+notExistList.toString()+",无法添加");
//            return result;
//        }
            chart.setState(StandardState.AVAILABLE.getValue());
            chart.setCreatedAt(new Date());
            chart.setUpdatedAt(new Date());
            result.setCode(ResultCodeEnum.SUCCESS.getCode());
            result.setData(chartDao.insert(chart));
        }
        return result;
    }

    @Override
    public BaseResult<PageData> getPageList(ChartDto obj) {
        return null;
    }

    public PageData getPageList2(ChartDto obj) {
        //db.blog.find({"comments":{"$elemMatch":{"author":"joe","score":{"$gte":5}}}})
        Pager pager = obj.getPager();
        if (!obj.validHandleUser()) {
            logger.info("经办人信息缺失");
            return null;
        }
        if (pager == null) {
            logger.info("分页信息为空");
            return null;
        } else {
            if (pager.getRownum() <= 0) {
                //todo
            }
        }

        Map<String, Object> params = MapAndBeanTransUtils.transBean2Map(obj);
//        params.put("state",StandardState.AVAILABLE.getValue());
//        Map visibleMap=new HashedMap();
//        Map nameMap=new HashedMap();
//        nameMap.put("userName",obj.getHandleUserName());
//        visibleMap.put(ComPareConstants.ELEMMATCH.getDisplayName(),nameMap);
//        params.put("visibleList", visibleMap);

        return getPageResult(chartDao, params, pager.getStart(), pager.getRownum());

//        List<Chart>  userVisibleChartList=chartDao.findByPager(params, pager.getStart(), pager.getRownum());
//        long totalCount=chartDao.countByParams(params);
//        PageData pageData=new PageData();
//        pageData.setTotalRecords(totalCount);
//        pageData.setRownum(pager.getRownum());
//        pageData.setCurrentPage(pager.getCurrentPage());
//        pageData.setRows(userVisibleChartList);
//        return pageData;
    }

    private List<Chart> getUserVisibleChartList(List<Chart> chartList, String handleUserName) {
        List<Chart> userVisibleChartList = new ArrayList<>();
        for (Chart chart : chartList) {
            String visibleType = chart.getVisibleType();
            if (VisibleTypeEnum.SPECIFICRANGE.name().equals(visibleType)) {//指定人可见
                List<String> visibleUserNameList = chart.getVisibleList();
                if (!visibleUserNameList.contains(handleUserName)) {
                    logger.info("该用户没有该图的查看权限,chartCode=" + chart.getChartCode() + ",handleUserName=" + handleUserName);
                    continue;
                } else {
                    userVisibleChartList.add(chart);
                }
            }
        }
        return userVisibleChartList;
    }

    @Override
    public BaseResult<ChartDto> queryItemByCode(String itemCode, String handleUserName) {//是否要查询已失效的图
        BaseResult<ChartDto> result = new BaseResult<>();
        if (StringUtils.isEmpty(itemCode)) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        Chart qryParams = new Chart();
        qryParams.setChartCode(itemCode);
        Chart chart = chartDao.findOne(qryParams);
        List<Chart> baseChartList = new ArrayList<>();
        baseChartList.add(chart);
        //获取该用户可见范围内的图
        List<Chart> visibleChartList = getUserVisibleChartList(baseChartList, handleUserName);
        ChartDto chartDto = null;
        if (visibleChartList != null && visibleChartList.size() > 0) {
            chartDto = new ChartDto();
            BeanUtils.copyProperties(visibleChartList.get(0), chartDto);
        }
        //前者赋值给后者
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setData(chartDto);
        return result;
    }

    @Override
    public BaseResult<String> updateItem(ChartDto obj) {
        BaseResult<String> result = new BaseResult<>();
        String itemCode = obj.getChartCode();
        if (StringUtils.isEmpty(itemCode) || StringUtils.isEmpty(obj.getCreateUserName())) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        Map chartMap = MapAndBeanTransUtils.transBean2Map(obj);
        chartDao.updateOne(itemCode, chartMap);
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        return result;
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

    //    private  Set<String> findAllUserName(Set<String> visibleSet){
//        //db.test.find({"name":{"$in":["stephen","stephen1"]}})
//        Map userMap=new HashedMap();
//        Map userValueMap=new HashedMap();
//        userValueMap.put(ComPareConstants.IN.getDisplayName(),visibleSet);
//        userMap.put("userName",userValueMap);
//        List<User> userRangeList=userDao.findAll(userMap);
//
//        Set<String> findUserSet= new HashSet();//查询出来的用户
//        for (User user:userRangeList) {
//            findUserSet.add(user.getUserName());
//        }
//        return findUserSet;
//    }
}
