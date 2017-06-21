package org.loxf.metric.dal.dao;


import org.loxf.metric.dal.po.BoardChartRel;
import org.loxf.metric.dal.po.Chart;

import java.util.List;

public interface BoardChartRelMapper {
    List<Chart> getChartsByBoardId(String boardId);
    int insert(BoardChartRel record);
    int delete(String boardId);
}