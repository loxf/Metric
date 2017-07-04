package org.loxf.metric.dal.dao;

import org.apache.ibatis.annotations.Param;
import org.loxf.metric.dal.po.QuotaScan;

import java.util.List;

public interface QuotaScanMapper {
    int deleteByQuotaScanId(String id);
    int insert(QuotaScan record);
    int updateByQuotaScanId(QuotaScan record);
    List<QuotaScan> listQuotaScan(QuotaScan quotaScan);
    int countQuotaScan(QuotaScan record);
    List<QuotaScan> listUserQuotaScanRel(@Param("userId") String userId, @Param("busiDomain") String busiDomain);
    QuotaScan getQuotaScanById(String quotaScanId);
}