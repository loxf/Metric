package org.loxf.metric.service;

import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.TargetNoticeUserDto;
import org.loxf.metric.dal.po.TargetNoticeUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by caiyang on 2017/5/4.
 */
@Component
public class TargetNoticeUserManager {
    @Autowired
    private TargetNoticeUserMapper targetNoticeUserMapper;

    public List<TargetNoticeUserDto> getTargetNoticeUserDto(String targetId) {
        List<TargetNoticeUserDto> results=new ArrayList<>();
        List<TargetNoticeUser> targetNoticeUsers=targetNoticeUserMapper.selectByTargetId(targetId);
        if(!CollectionUtils.isEmpty(targetNoticeUsers)){
            for(TargetNoticeUser tnu:targetNoticeUsers){
                TargetNoticeUserDto dto=new TargetNoticeUserDto();
                BeanUtils.copyProperties(tnu, dto);
                results.add(dto);
            }
        }

        return results;
    }
    @Transactional
    public BaseResult<String> createNoticeUser(List<TargetNoticeUserDto> targetNoticeUserDtos, String targetId) {

        List<TargetNoticeUser> targetNoticeUsers=new ArrayList<>();
        if(!CollectionUtils.isEmpty(targetNoticeUserDtos)){
            for(TargetNoticeUserDto targetNoticeUserDto:targetNoticeUserDtos){
                TargetNoticeUser tnu=new TargetNoticeUser();
                BeanUtils.copyProperties(targetNoticeUserDto, tnu);
                tnu.setCreatedAt(new Date());
                tnu.setTargetId(targetId);
                targetNoticeUsers.add(tnu);
            }
        }
        targetNoticeUserMapper.batchInsert(targetNoticeUsers);
        return new BaseResult<>();
    }

    @Transactional
    public int deleteByTargetId(String targetId) {
        return targetNoticeUserMapper.deleteByTargetId(targetId);
    }

}
