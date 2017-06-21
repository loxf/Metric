package org.loxf.metric.dal.dao;

import org.apache.ibatis.annotations.Param;
import org.loxf.metric.dal.po.SyncJobInst;

import java.util.Date;
import java.util.List;

public interface SyncJobInstMapper {
    int insert(SyncJobInst record);

    int updateInst(SyncJobInst record);

    int delete(@Param("jobCode") String jobCode);

    int existsSyncInst(@Param("jobCode") String jobCode, @Param("circleTime") Date circleTime);

    List<SyncJobInst> getWaitingInst(String jobCode);

    List<SyncJobInst> getExecutingInst(String jobCode);

    int recoverySyncJobInst(@Param("jobCode") String jobCode, @Param("circleTime") Date circleTime);

    int recoveryAllSyncJobInst(@Param("jobCode") String jobCode);

    int cleanDataByCircleTime(@Param("circleTime") Date circleTime, @Param("dataTable") String dataTable);

    int cleanDataTable(@Param("dataTable") String dataTable);

    List<SyncJobInst> getSyncJobInst(SyncJobInst jobInst);

    int getSyncJobInstCount(SyncJobInst jobInst);
}