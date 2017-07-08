package org.loxf.metric.service;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.TargetDto;
import org.loxf.metric.dal.dao.interfaces.TargetDao;
import org.loxf.metric.dal.po.Target;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by caiyang on 2017/5/4.
 */
@Component
public class TargetManager {

    @Autowired
    private TargetDao targetDao;

    public String insert(TargetDto targetDto){
        Target target = new Target();
        BeanUtils.copyProperties(targetDto, target);
        target.setCreatedAt(new Date());
        target.setUpdatedAt(new Date());
        return targetDao.insert(target);
    }

    public TargetDto getTagetDtoByParams(Map<String, Object> params){
        TargetDto targetDto=new TargetDto();
        Target target = targetDao.findOne(params);
        BeanUtils.copyProperties(target, targetDto);
        return targetDto;
    }

    public Target getTargetByParams(Map<String, Object> params){
        return targetDao.findOne(params);
    }

    public void updateByCode(String targetCode,Map<String, Object> setParams){
        Map<String, Object> qryParams=new HashedMap();
        qryParams.put("targetCode",targetCode);
        setParams.put("updatedAt",new Date());
        targetDao.update(qryParams,setParams);
    }

    public List<TargetDto> listTargetByUser(String busiDomain, String objType, String objId, String startCircleTime, String endCircleTime){
        //List<Target> listTarget = targetDto.listTargetByUser(busiDomain, objType, objId, startCircleTime, endCircleTime);
        return null;
    }
    @Transactional
    public BaseResult<String> delTargetByCode(String targetCode) {
        Map map = new HashMap<>();
        map.put("targetCode", targetCode);
        targetDao.remove(map);
        return new BaseResult<>();
    }
}
