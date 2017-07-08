package org.loxf.metric.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
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
    @Autowired
    private BoardDao boardDao;

    public String insert(BoardDto boardDto) {
        Board board = new Board();
        BeanUtils.copyProperties(boardDto, board);
        board.setCreatedAt(new Date());
        board.setUpdatedAt(new Date());
        return boardDao.insert(board);
    }

    public BoardDto getBoardDtoByParams(Map<String, Object> params) {
        BoardDto boardDto=new BoardDto();
        Board board = getBoardByParams(params);
        BeanUtils.copyProperties(getBoardByParams(params), boardDto);
        return boardDto;
    }

    public Board getBoardByParams(Map<String, Object> params) {
        return boardDao.findOne(params);
    }

    public void updateBoard(String boardCode,Map<String, Object> setParams) {
        Map<String, Object> qryParams=new HashedMap();
        qryParams.put("boardCode",boardCode);
        setParams.put("updatedAt",new Date());
        boardDao.update(qryParams,setParams);
    }

    public void delBoardByCode(String boardCode) {
        Map<String, Object> delParams=new HashedMap();
        delParams.put("boardCode",boardCode);
        boardDao.remove(delParams);
    }
}
