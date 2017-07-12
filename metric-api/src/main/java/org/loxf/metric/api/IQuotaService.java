package org.loxf.metric.api;

import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.QuotaDto;

import java.util.List;

/**
 * Created by hutingting on 2017/7/7.
 */
public interface IQuotaService extends IBaseService<QuotaDto> {
    /**
     * 检查指标依赖
     * @param quotaCode
     * @return
     */
    public BaseResult<String> checkDependencyQuota(String quotaCode);
    /**
     * 获取全部指标
     * @param quotaDto
     * @return
     */
    public BaseResult<List<QuotaDto>> queryQuotaList(QuotaDto quotaDto);
}
