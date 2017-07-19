package org.loxf.metric.api;

import org.loxf.metric.base.ItemList.ChartItem;
import org.loxf.metric.common.dto.BaseResult;

import java.util.List;

/**
 * Created by luohj on 2017/7/19.
 */
public interface IIndexSettingService {
    public BaseResult<String> updateSetting(List<ChartItem> data, String handlerUserName, String uniqueCode);
    public BaseResult<List<ChartItem>> getIndexSetting(String handlerUserName);
}
