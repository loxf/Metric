package org.loxf.metric.dal.dao;


import org.loxf.metric.dal.po.Board;

import java.util.List;

public interface BoardMapper {
    int deleteByBoardId(String boardId);

    int insert(Board record);

    List<Board> listBoard(Board record);

    int countBoard(Board record);

    Board selectByBoardId(String boardId);

    int updateByPrimaryKey(Board record);


}