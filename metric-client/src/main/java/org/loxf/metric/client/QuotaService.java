package org.loxf.metric.client;

import org.loxf.metric.common.dto.*;

import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/5/4.
 */
public interface QuotaService {
    public BaseResult<String> createQuota(QuotaDto quotaDto);

    public BaseResult<String> updateQuota(QuotaDto quotaDto);

    public BaseResult<String> delQuota(QuotaDto quotaDto);

    /**
     * 获取展示指标的值
     * @param scanId 展示指标ID
     * @param condition 条件
     * @return
     */
    public BaseResult getQuotaData(String scanId, ConditionVo condition);

    public ChartData getChartData(String chartId, ConditionVo condition);

    public BaseResult<List<QuotaDto>> queryQuotaNameAndId(QuotaDto quotaDto);

    public BaseResult<QuotaDto> getQuotaByCode(String quotaCode);

    public BaseResult<QuotaDto> getQuota(String quotaId);

    public List<QuotaDimensionDto> queryDimenListByChartId(String chartId);

    public List<QuotaDimensionDto> queryDimenListByBoardId(String boardId);

    public List<QuotaDimensionDto> queryDimenListByQuotaId(String quotaId);

    public PageData<QuotaDto> listQuotaPage(QuotaDto quotaDto);

    public List<Map> queryDimenListByQuotaCode(String[] quotaCodes);
}
