package org.loxf.metric.api;

import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.QuotaDimensionDto;

/**
 * Created by hutingting on 2017/7/7.
 */
public interface IQuotaDimensionService extends IBaseService<QuotaDimensionDto> {
    public BaseResult<String> delItemByCode(String itemCode, String uniqueCode, String handleUserName);
}
