package org.loxf.metric.api;

import org.loxf.metric.base.ItemList.ChartItem;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.BoardDto;
import org.loxf.metric.common.dto.UserDto;

import java.util.List;

/**
 * Created by hutingting on 2017/7/7.
 */
public interface IBoardService extends IBaseService<BoardDto> {
    public BaseResult<String>  addChartList(String boardCode, UserDto userDto, List<ChartItem> chartItemList);

    public BaseResult<String>  delChartList(String boardCode,String chartCodeList,String handleUserName);
/*
    public BaseResult<List<ChartItem>>  getOwnChartList(String boardCode,String handleUserName);
*/
    public BaseResult<List<ChartItem>> getOtherChartListByPage(BoardDto boardDto);

}
