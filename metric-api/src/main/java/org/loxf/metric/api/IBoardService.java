package org.loxf.metric.api;

import org.loxf.metric.base.ItemList.ChartItem;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.BoardDto;

import java.util.List;

/**
 * Created by hutingting on 2017/7/7.
 */
public interface IBoardService extends IBaseService<BoardDto> {
    public BaseResult<String>  addChart(String boardCode,String handleUserName,List<ChartItem> chartItemList);
    public BaseResult<String>  delChart(String boardCode,String chartCode,String handleUserName);
    public BaseResult<List<ChartItem>>  getOwnChartList(String boardCode,String handleUserName);
    public BaseResult<List<ChartItem>> getOtherChartListByPage(BoardDto boardDto);

}
