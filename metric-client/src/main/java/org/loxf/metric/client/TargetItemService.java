package org.loxf.metric.client;


import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.TargetItemDto;

import java.util.List;

/**
 * Created by caiyang on 2017/5/4.
 */
public interface TargetItemService {
    /**
     * 根据目标获取目标项列表
     * @param targetItemDtoDto
     * @return
     */
    public List<TargetItemDto> getTargetItemList(TargetItemDto targetItemDtoDto);


    /**
     * 批量创建目标项
     * @param targetItemDtos
     * @return
     */
    public BaseResult<String> createTargetItems(List<TargetItemDto> targetItemDtos, String targetId);

    /**
     * 删除目标项
     * @param targetId
     * @return
     */
    public  int deleteByTargetId(String targetId);
}
