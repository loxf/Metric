package org.loxf.metric.service.impl;


import com.alibaba.dubbo.common.utils.CollectionUtils;
import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.service.base.BaseService;
import org.loxf.metric.client.TargetService;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.TargetDto;
import org.loxf.metric.dal.po.Target;
import org.loxf.metric.service.TargetManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 目标配置业务类
 * Created by caiyang on 2017/5/4.
 */
@Service("targetService")
public class TargetServiceImpl extends BaseService implements TargetService {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private TargetManager targetManager;

    @Override
    public PageData listTargetPage(TargetDto targetDto) {
        Target target = new Target();
        BeanUtils.copyProperties(targetDto, target);
        PageData pageUtilsUI = null; //super.pageList(target, TargetMapper.class, "Target");
        List<Target> targetList = pageUtilsUI.getRows();
        List<TargetDto> targetDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(targetList)) {
            for (Target t : targetList) {
                TargetDto dto = new TargetDto();
                BeanUtils.copyProperties(t, dto);
                targetDtoList.add(dto);
            }
        }
        pageUtilsUI.setRows(targetDtoList);
        return pageUtilsUI;
    }

    @Override
    public BaseResult<String> createTarget(TargetDto targetDto) {
        try {
            //创建目标
            String sid = targetManager.insert(targetDto);
            return new BaseResult<String>(sid);
        } catch (Exception e) {
            logger.error("创建目标失败", e);
            throw new MetricException("创建目标失败", e);
        }
    }

    @Override
    public BaseResult<String> updateTarget(TargetDto targetDto) {
        try {
            //创建目标
            String i = targetManager.update(targetDto);
            return new BaseResult<String>(i);
        } catch (Exception e) {
            logger.error("更新目标失败", e);
            throw new MetricException("更新目标失败", e);
        }
    }


    public List<TargetDto> listTargetByUser(String busiDomain, String objType, String objId, String startCircleTime, String endCircleTime) {
        return targetManager.listTargetByUser(busiDomain, objType, objId, startCircleTime, endCircleTime);
    }

    @Override
    public BaseResult<String> delTarget(String targetId) {
        return targetManager.delTarget(targetId);
    }
}
