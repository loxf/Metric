package org.loxf.metric.dal.dao;

import org.apache.ibatis.annotations.Param;
import org.loxf.metric.dal.po.QuotaDimension;

import java.util.List;

public interface QuotaDimensionMapper {
    int insert(QuotaDimension record);

    int deleteByQuotaId(String quotaId);

    List<QuotaDimension> selectByQuotaId(String quotaId);

    String selectByDimCode(@Param("quotaId") String quotaId, @Param("columnCode") String columnCode);
}