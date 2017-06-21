package org.loxf.metric.dal.dao;

import org.apache.ibatis.annotations.Param;
import org.loxf.metric.dal.po.UserQuotaScanRel;

import java.util.List;

public interface UserQuotaScanRelMapper {
    /**
     * 根据用户ID删除用户关联的展示指标
     * @param userId
     * @return
     */
    int deleteByUserId(@Param("userId") String userId, @Param("domain") String domain);

    /**
     * 批量插入用户指标关联
     * @param list
     * @return
     */
    int batchCreateUserQuotaScanRel(List<UserQuotaScanRel> list);

    /**
     * 根据用户ID查询关联列表
     * @param po
     * @return
     */
    List<UserQuotaScanRel> listUserQuotaScanRel(UserQuotaScanRel po);
}