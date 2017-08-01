package org.loxf.metric.api;

import org.loxf.metric.base.ItemList.ChartItem;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.UserDto;

import java.util.List;

/**
 * Created by luohj on 2017/7/19.
 */
public interface IIndexSettingService {
    public BaseResult<String> addOrUpdateSetting(List<ChartItem> data, String handlerUserName, String uniqueCode);
    public BaseResult<List<ChartItem>> getIndexSetting(String handlerUserName);
    public BaseResult<String>  addChartList(String indexCode,UserDto handleUserDto,List<ChartItem> chartItemList);
    public BaseResult<String>  delChartList(String indexCode,UserDto handleUserDto,String chartCodeList);
}
