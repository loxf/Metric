package org.loxf.metric.dal.dao;

import org.loxf.metric.dal.po.TargetNoticeUser;

import java.util.List;

public interface TargetNoticeUserMapper {
    int insert(TargetNoticeUser record);
    List<TargetNoticeUser> selectByTargetId(String targetId);
    int deleteByTargetId(String targetId);
    int batchInsert(List<TargetNoticeUser> targetNoticeUsers);
}