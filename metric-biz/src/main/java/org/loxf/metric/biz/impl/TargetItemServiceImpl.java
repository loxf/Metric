package org.loxf.metric.biz.impl;


import org.loxf.metric.biz.base.BaseService;
import org.loxf.metric.client.TargetItemService;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.TargetItemDto;
import org.loxf.metric.service.TargetItemManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 目标项配置业务类
 * Created by caiyang on 2017/5/4.
 */
@Service("targetItemService")
public class TargetItemServiceImpl extends BaseService implements TargetItemService{
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private TargetItemManager targetItemManager;

    @Override
    public List<TargetItemDto> getTargetItemList(TargetItemDto targetItemDtoDto) {
        return targetItemManager.getTargetItemList(targetItemDtoDto);
    }

    @Override
    public BaseResult<String> createTargetItems(List<TargetItemDto> targetItemDtos, String targetId) {
        targetItemManager.createTargetItems(targetItemDtos,targetId);
        return new BaseResult<>();
    }

    @Override
    public int deleteByTargetId(String targetId) {
        return targetItemManager.deleteByTargetId(targetId);
    }
}
