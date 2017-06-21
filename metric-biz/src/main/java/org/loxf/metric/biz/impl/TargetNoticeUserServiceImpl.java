package org.loxf.metric.biz.impl;

import org.loxf.metric.biz.base.BaseService;
import org.loxf.metric.client.TargetNoticeUserService;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.TargetNoticeUserDto;
import org.loxf.metric.service.TargetNoticeUserManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 目标项配置业务类
 * Created by caiyang on 2017/5/4.
 */
@Service("targetNoticeUserService")
public class TargetNoticeUserServiceImpl extends BaseService implements TargetNoticeUserService{
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private TargetNoticeUserManager targetNoticeUserManager;

    @Override
    public List<TargetNoticeUserDto> getTargetNoticeUserDto(String targetId) {
        return targetNoticeUserManager.getTargetNoticeUserDto(targetId);
    }

    @Override
    public BaseResult<String> createNoticeUser(List<TargetNoticeUserDto> targetNoticeUserDto, String targetId) {
        return targetNoticeUserManager.createNoticeUser(targetNoticeUserDto,targetId);
    }

    @Override
    public int deleteByTargetId(String targetId) {
        return targetNoticeUserManager.deleteByTargetId(targetId);
    }
}
