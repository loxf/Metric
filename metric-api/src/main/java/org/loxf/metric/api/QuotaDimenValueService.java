package org.loxf.metric.api;


import org.loxf.metric.common.dto.QuotaDimensionValueDto;

import java.util.List;

/**
 * Created by luohj on 2017/5/15.
 */
public interface QuotaDimenValueService {
    public List<QuotaDimensionValueDto> queryValueByColumnCode(String columnCode);
}
