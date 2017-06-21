package org.loxf.metric.dal.dao;


import org.loxf.metric.dal.po.TargetItem;

import java.util.List;

public interface TargetItemMapper {
    /**
     * 批量插入目标项
     * @param list
     */
    void batchInsert(List<TargetItem> list);

    /**
     * 根据目标ID删除关联指标项
     * @param targetId
     * @return
     */
    int deleteByTargetId(String targetId);

    /**
     * 根据目标ID获取目标项列表
     * @param targetItem
     * @return
     */
    List<TargetItem> getTargetItemList(TargetItem targetItem);

    /**
     * 更新目标项
     * @param targetItem
     * @return
     */
    int updateTagetItem(TargetItem targetItem);

}