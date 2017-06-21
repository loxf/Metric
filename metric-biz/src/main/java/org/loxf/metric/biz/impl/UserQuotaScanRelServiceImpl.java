package org.loxf.metric.biz.impl;

import org.loxf.metric.biz.base.BaseService;
import org.loxf.metric.client.UserQuotaScanRelService;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.UserQuotaScanRelDto;
import org.loxf.metric.service.UserQuotaScanRelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by caiyang on 2017/5/4.
 */
@Service("userQuotaScanRelService")
public class UserQuotaScanRelServiceImpl extends BaseService implements UserQuotaScanRelService {
    private static Logger logger = LoggerFactory.getLogger(UserQuotaScanRelServiceImpl.class);

    @Autowired
    private UserQuotaScanRelManager userQuotaScanRelManager;


    @Override
    public BaseResult<String> batchCreateUserQuotaScanRel(List<UserQuotaScanRelDto> userQuotaScanRelDto, String domain) {
        return userQuotaScanRelManager.batchCreateUserQuotaScanRel(userQuotaScanRelDto, domain);
    }

    @Override
    public List<UserQuotaScanRelDto> listUserQuotaScanRel(UserQuotaScanRelDto userQuotaScanRelDto) {
        return userQuotaScanRelManager.listUserQuotaScanRel(userQuotaScanRelDto);
    }

    @Override
    public int delQuotaScanRelByUserId(String userId, String domain) {
        return userQuotaScanRelManager.delQuotaScanRelByUserId(userId, domain);
    }
}
