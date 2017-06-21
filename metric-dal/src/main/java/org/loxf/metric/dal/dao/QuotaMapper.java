package org.loxf.metric.dal.dao;

import org.apache.ibatis.annotations.Param;
import org.loxf.metric.dal.po.Quota;
import org.loxf.metric.dal.po.QuotaDimension;

import java.util.List;
import java.util.Map;

public interface QuotaMapper {
    int insert(Quota record);

    Quota selectQuota(Quota quota);

    List<Quota> selectList(Quota quota);

    int count(Quota quota);

    int update(Quota record);

    List<Map> queryBySql(@Param("sql") String sql);

    List<Quota> queryQuotaNameAndId(Quota quota);

    List<QuotaDimension> queryDimenListByChartId(@Param("chartId") String chartId);

    List<QuotaDimension> queryDimenListByBoardId(@Param("boardId") String boardId);

    List<QuotaDimension> queryDimenListByQuotaId(@Param("quotaId") String quotaId);

    List<Map> queryDimenListByQuotaCodes(@Param("quotaCodes") String[] quotaCodes);

    int deleteByQuotaId(String quotaId);

    List<Quota> queryQuotaDependency(String quotaStr);
}