package org.loxf.metric.client;


import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.UserQuotaScanRelDto;

import java.util.List;

public interface UserQuotaScanRelService {
    /**
     * 创建展示指标与用户关系
     * @param userQuotaScanRelDto
     * @return
     */
    public BaseResult<String> batchCreateUserQuotaScanRel(List<UserQuotaScanRelDto> userQuotaScanRelDto, String domain);

    /**
     * 获取用户关系列表
     * @param userQuotaScanRelDto
     * @return
     */
    public List<UserQuotaScanRelDto> listUserQuotaScanRel(UserQuotaScanRelDto userQuotaScanRelDto);

    /**
     * 删除用户关系
      * @param userId
     * @return
     */
    public  int delQuotaScanRelByUserId(String userId, String domain);

}