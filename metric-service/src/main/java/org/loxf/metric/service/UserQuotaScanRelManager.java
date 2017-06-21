package org.loxf.metric.service;

import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.UserQuotaScanRelDto;
import org.loxf.metric.dal.dao.UserQuotaScanRelMapper;
import org.loxf.metric.dal.po.UserQuotaScanRel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by caiyang on 2017/5/4.
 */
@Component
public class UserQuotaScanRelManager {
    private static Logger logger = LoggerFactory.getLogger(UserQuotaScanRelManager.class);
    @Autowired
    private UserQuotaScanRelMapper userQuotaScanRelMapper;


    /**
     * 创建展示指标与用户关系
     * @param userQuotaScanRelDto
     * @return
     */
    @Transactional
    public BaseResult<String> batchCreateUserQuotaScanRel(List<UserQuotaScanRelDto> userQuotaScanRelDto, String domain){
        try {
            if(!CollectionUtils.isEmpty(userQuotaScanRelDto)){
                userQuotaScanRelMapper.deleteByUserId(userQuotaScanRelDto.get(0).getUserId(), domain);
                List<UserQuotaScanRel> userQuotaScanRels=new ArrayList<>();
                for(UserQuotaScanRelDto dto:userQuotaScanRelDto){
                    UserQuotaScanRel po=new UserQuotaScanRel();
                    BeanUtils.copyProperties(dto,po);
                    userQuotaScanRels.add(po);
                }
                userQuotaScanRelMapper.batchCreateUserQuotaScanRel(userQuotaScanRels);
                return new BaseResult<>("常见用户指标预览成功");
            }
        } catch (MetricException e) {
            logger.error("批量插入用户指标预览失败！"+e);
            throw new MetricException("批量插入用户指标预览失败！", e);
        }
        return new BaseResult<>("常见用户指标预览成功");
    }
    /**
     * 获取用户关系列表
     * @param userQuotaScanRelDto
     * @return
     */
    public List<UserQuotaScanRelDto> listUserQuotaScanRel(UserQuotaScanRelDto userQuotaScanRelDto){
        List<UserQuotaScanRelDto> resultList=new ArrayList<>();
        try {
            UserQuotaScanRel po=new UserQuotaScanRel();
            BeanUtils.copyProperties(userQuotaScanRelDto,po);
            List<UserQuotaScanRel> userQuotaScanRels=  userQuotaScanRelMapper.listUserQuotaScanRel(po);
            if(CollectionUtils.isEmpty(userQuotaScanRels)){
                for(UserQuotaScanRel userQuotaScanRel:userQuotaScanRels){
                    UserQuotaScanRelDto dto=new UserQuotaScanRelDto();
                    BeanUtils.copyProperties(userQuotaScanRel,dto);
                    resultList.add(dto);
                }
            }
        } catch (MetricException e) {
            logger.error("获取用户指标预览列表失败！"+e);
            throw new MetricException("获取用户指标预览列表失败！", e);
        }
        return  resultList;
    }
    /**
     * 删除用户关系
     * @param userId
     * @return
     */
    @Transactional
    public  int delQuotaScanRelByUserId(String userId, String domain){
        return userQuotaScanRelMapper.deleteByUserId(userId, domain);
    }

}
