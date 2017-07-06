package org.loxf.metric.service;

import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.TargetDto;
import org.loxf.metric.common.dto.TargetItemDto;
import org.loxf.metric.dal.dao.interfaces.TargetDao;
import org.loxf.metric.dal.po.Target;
import org.loxf.metric.dal.po.TargetItem;
import org.apache.commons.collections.CollectionUtils;
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
    private static String target_prefix = "TG_";
    @Autowired
    private TargetDao targetDao;

    @Transactional
    public String insert(TargetDto targetDto){
        Target target=new Target();
        BeanUtils.copyProperties(targetDto, target);
        String sid = IdGenerator.generate(target_prefix);
        target.setTargetId(sid);
        target.setCreatedAt(new Date());
        target.setUpdatedAt(new Date());
        targetDao.insert(target);
        return sid;
    }

    @Transactional
    public String update(TargetDto targetDto){
        Target target=new Target();
        BeanUtils.copyProperties(targetDto, target);
        target.setUpdatedAt(new Date());
        // targetDto.update(target);
        return null;
    }

    public List<TargetDto> listTargetByUser(String busiDomain, String objType, String objId, String startCircleTime, String endCircleTime){
        //List<Target> listTarget = targetDto.listTargetByUser(busiDomain, objType, objId, startCircleTime, endCircleTime);
        return null;
    }
    @Transactional
    public BaseResult<String> delTarget(String targetId) {
        Map map = new HashMap<>();
        map.put("targetId", targetId);
        targetDao.remove(map);
        return new BaseResult<>();
    }
}
