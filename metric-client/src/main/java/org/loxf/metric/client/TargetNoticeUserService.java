package org.loxf.metric.client;


import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.TargetNoticeUserDto;

import java.util.List;

/**
 * Created by caiyang on 2017/5/4.
 */
public interface TargetNoticeUserService {
    /**
     * 根据目标获取目标关注人信息
     * @param targetId
     * @return
     */
    public List<TargetNoticeUserDto> getTargetNoticeUserDto(String targetId);


    /**
     * 创建目标关注人关系
     * @param targetNoticeUserDto
     * @return
     */
    public BaseResult<String> createNoticeUser(List<TargetNoticeUserDto> targetNoticeUserDto, String targetId);

    /**
     * 删除目标关注人
     * @param targetId
     * @return
     */
    public  int deleteByTargetId(String targetId);
}
