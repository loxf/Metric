package org.loxf.metric.service;

import org.apache.commons.collections.CollectionUtils;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.BoardDto;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.dal.dao.interfaces.BoardDao;
import org.loxf.metric.dal.po.Board;
import org.loxf.metric.dal.po.BoardChartRel;
import org.loxf.metric.dal.po.Chart;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by caiyang on 2017/5/4.
 */
@Component
public class BoardManager {
    private static String board_prefix = "BD_";
    @Autowired
    private BoardDao boardDao;

    @Transactional
    public String insert(BoardDto boardDto) {
        Board board = new Board();
        BeanUtils.copyProperties(boardDto, board);
        String sid = IdGenerator.generate(board_prefix);
        board.setBoardId(sid);
        board.setCreatedAt(new Date());
        board.setUpdatedAt(new Date());
        boardDao.insert(board);
        return sid;
    }

    public BoardDto getBoardByBoardId(String boardId) {
        BoardDto boardDto = new BoardDto();
        // Board board = boardDao.selectByBoardId(boardId);
        return boardDto;
    }
    @Transactional
    public BaseResult<String> updateBoard(BoardDto boardDto) {
        Board board=new Board();
        BeanUtils.copyProperties(boardDto, board);
        // boardDao.update(board);
        return new BaseResult<>();
    }
    @Transactional
    public BaseResult<String> delBoard(String boardId) {
        Map map = new HashMap<>();
        map.put("boardId", boardId);
        boardDao.remove(map);
        return new BaseResult<>();

    }
}
