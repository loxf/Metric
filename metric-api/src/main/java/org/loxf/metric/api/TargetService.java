package org.loxf.metric.api;


import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.TargetDto;

import java.util.List;

/**
 * Created by caiyang on 2017/5/4.
 */
public interface TargetService {
    /**
     * 获取目标列表支持分页
     * @param targetDto
     * @return
     */
    public PageData listTargetPage(TargetDto targetDto);


    /**
     * 创建目标
     * @param targetDto
     * @return
     */
    public BaseResult<String> createTarget(TargetDto targetDto);

    /**
     * 更新目标
     * @param targetDto
     * @return
     */
    public BaseResult<String> updateTarget(TargetDto targetDto);

    public List<TargetDto> listTargetByUser(String busiDomain, String objType, String objId, String startCircleTime, String endCircleTime);

    /**
     * 删除目标
     * @param targetId
     * @return
     */
    public BaseResult<String> delTarget(String targetId);
}
