package org.loxf.metric.dal.dao;

import org.loxf.metric.dal.po.QuotaDimensionValue;

import java.util.List;

public interface QuotaDimensionValueMapper {

    int insert(QuotaDimensionValue record);

    int exists(QuotaDimensionValue record);

    List<QuotaDimensionValue> queryValueByColumnCode(String columnCode);
}