package org.loxf.metric.service.impl;


import com.alibaba.dubbo.common.utils.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.api.ITargetService;
import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.common.dto.*;
import org.loxf.metric.common.utils.MapAndBeanTransUtils;
import org.loxf.metric.dal.dao.interfaces.TargetDao;
import org.loxf.metric.dal.po.Target;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 目标配置业务类
 * Created by caiyang on 2017/5/4.
 */
@Service("targetService")
public class TargetServiceImpl implements ITargetService {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private TargetDao targetDao;

    @Override
    public BaseResult<String> insertItem(TargetDto obj) {
        Target target=new Target();
        BeanUtils.copyProperties(obj,target);
        target.setCreatedAt(new Date());
        target.setUpdatedAt(new Date());
        return new BaseResult<>(targetDao.insert(target));
    }

    @Override
    public PageData getPageList(TargetDto obj) {
        Pager pager=obj.getPager();
        if(pager==null){
            logger.info("分页信息为空，无法查询!");
            return null;
        }
        Map<String, Object> params= MapAndBeanTransUtils.transBean2Map(obj);

        List<Target>  targetList=targetDao.findByPager(params, pager.getStart(), pager.getRownum());
        PageData pageData=new PageData();
        pageData.setTotalRecords(targetList.size());
        pageData.setRownum(pager.getRownum());
        pageData.setCurrentPage(pager.getCurrentPage());
        pageData.setRows(targetList);
        return pageData;
    }

    @Override
    public BaseResult<TargetDto> queryItemByCode(String itemCode,String handleUserName) {
        Map<String, Object> qryParams=new HashedMap();
        qryParams.put("targetCode",itemCode);
        Target target=targetDao.findOne(qryParams);
        TargetDto targetDto=new TargetDto();
        BeanUtils.copyProperties(target,targetDto);//前者赋值给后者
        return new BaseResult<>(targetDto);
    }

    @Override
    public BaseResult<String> updateItem(TargetDto obj) {
        String itemCode=obj.getTargetCode();
        if(StringUtils.isEmpty(itemCode)){
            return new BaseResult<>("targetCode不能为空！");
        }
        Map targetMap= MapAndBeanTransUtils.transBean2Map(obj);
        targetDao.updateOne(itemCode,targetMap);
        return new BaseResult<>();
    }

    @Override
    public BaseResult<String> delItemByCode(String itemCode,String handleUserName) {
        targetDao.remove(itemCode);
        return new BaseResult<>();
    }

}
