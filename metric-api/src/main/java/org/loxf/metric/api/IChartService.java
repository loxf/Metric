package org.loxf.metric.api;

import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.common.dto.PageData;

import java.util.List;

/**
 * Created by hutingting on 2017/7/6.
 */
public interface IChartService extends IBaseService<ChartDto> {
    public BaseResult<PageData<ChartDto>> getPageListExcludeChartList(ChartDto obj, List<String> excludeChartList);
}
