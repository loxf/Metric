package org.loxf.metric.service;

import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.TargetDto;
import org.loxf.metric.common.dto.TargetItemDto;
import org.loxf.metric.dal.dao.TargetItemMapper;
import org.loxf.metric.dal.dao.TargetMapper;
import org.loxf.metric.dal.dao.TargetNoticeUserMapper;
import org.loxf.metric.dal.po.Target;
import org.loxf.metric.dal.po.TargetItem;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by caiyang on 2017/5/4.
 */
@Component
public class TargetManager {
    private static String target_prefix = "TG_";
    @Autowired
    private TargetMapper targetMapper;
    @Autowired
    private TargetItemMapper targetItemMapper;
    @Autowired
    private TargetNoticeUserMapper targetNoticeUserMapper;
    @Transactional
    public String insert(TargetDto targetDto){
        Target target=new Target();
        BeanUtils.copyProperties(targetDto, target);
        String sid = IdGenerator.generate(target_prefix);
        target.setTargetId(sid);
        target.setCreatedAt(new Date());
        target.setUpdatedAt(new Date());
        int i=  targetMapper.insert(target);
        return sid;
    }

    @Transactional
    public String update(TargetDto targetDto){
        Target target=new Target();
        BeanUtils.copyProperties(targetDto, target);
        target.setUpdatedAt(new Date());
        int i=  targetMapper.update(target);
        return i+"";
    }

    public List<TargetDto> listTargetByUser(String busiDomain, String objType, String objId, String startCircleTime, String endCircleTime){
        List<Target> listTarget = targetMapper.listTargetByUser(busiDomain, objType, objId, startCircleTime, endCircleTime);
        if(CollectionUtils.isNotEmpty(listTarget)){
            List<TargetDto> targetDtoList = new ArrayList<>();
            for (Target target:listTarget){
                TargetDto dto = new TargetDto();
                BeanUtils.copyProperties(target, dto);
                // 获取目标项
                TargetItem item = new TargetItem();
                item.setTargetId(target.getTargetId());
                List<TargetItem> targetItemList = targetItemMapper.getTargetItemList(item);
                if(CollectionUtils.isNotEmpty(targetItemList)) {
                    List<TargetItemDto> targetItemDtoList = new ArrayList<>();
                    for (TargetItem targetItem : targetItemList) {
                        TargetItemDto itemDto = new TargetItemDto();
                        BeanUtils.copyProperties(targetItem, itemDto);
                        targetItemDtoList.add(itemDto);
                    }
                    dto.setTargetItemDtoLists(targetItemDtoList);
                }
                targetDtoList.add(dto);
            }
            return targetDtoList;
        }
        return null;
    }
    @Transactional
    public BaseResult<String> delTarget(String targetId) {
     int i=   targetMapper.delete(targetId);
     if(i>0){
         //删除关联指标 即目标项
         int j=targetItemMapper.deleteByTargetId(targetId);
         //删除目标关注人
         int k=targetNoticeUserMapper.deleteByTargetId(targetId);

         return new BaseResult<>(i+"");
     }
         return new BaseResult<>(0,-1+"");
    }
}
