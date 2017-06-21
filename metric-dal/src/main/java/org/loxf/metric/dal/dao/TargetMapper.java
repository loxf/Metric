package org.loxf.metric.dal.dao;

import org.apache.ibatis.annotations.Param;
import org.loxf.metric.dal.po.Target;

import java.util.List;

public interface TargetMapper {

    int insert(Target target);

    List<Target> listTarget(Target target);

    int countTarget(Target target);

    int update(Target target);

    int delete(String targetId);

    List<Target> listTargetByUser(@Param("busiDomain") String busiDomain, @Param("objType") String objType, @Param("objId") String objId,
                                  @Param("startCircleTime") String startCircleTime, @Param("endCircleTime") String endCircleTime);

}