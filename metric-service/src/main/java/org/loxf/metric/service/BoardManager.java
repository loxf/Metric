package org.loxf.metric.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.BoardDto;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.dal.dao.BoardChartRelMapper;
import org.loxf.metric.dal.dao.BoardMapper;
import org.loxf.metric.dal.dao.interfaces.BoardChartRelDao;
import org.loxf.metric.dal.po.Board;
import org.loxf.metric.dal.po.BoardChartRel;
import org.loxf.metric.dal.po.Chart;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by caiyang on 2017/5/4.
 */
@Component
public class BoardManager {
    private static String board_prefix = "BD_";
    @Autowired
    private BoardMapper boardMapper;
    @Autowired
    private BoardChartRelDao boardChartRelMapper;

    @Transactional
    public String insert(BoardDto boardDto) {
        Board board = new Board();
        BeanUtils.copyProperties(boardDto, board);
        String sid = IdGenerator.generate(board_prefix);
        board.setBoardId(sid);
        List<ChartDto> chartDtos=  boardDto.getChartList();
        if(CollectionUtils.isNotEmpty(chartDtos)){
            for(ChartDto chartDto: chartDtos) {
                BoardChartRel boardChartRel = new BoardChartRel();
                boardChartRel.setBoardId(sid);
                boardChartRel.setChartId(chartDto.getChartId());
                boardChartRel.setCreatedAt(new Date());
                boardChartRelMapper.insert(boardChartRel);
            }
        }
        board.setCreatedAt(new Date());
        board.setUpdatedAt(new Date());
        boardMapper.insert(board);
        return sid;
    }

    public BoardDto getBoardByBoardId(String boardId) {
        BoardDto boardDto = new BoardDto();
        Board board = boardMapper.selectByBoardId(boardId);
        // 根据看板去查询图信息
        if (board != null) {
            BeanUtils.copyProperties(board, boardDto);
            Map qryParams=new HashedMap();
            qryParams.put("boardId",boardId);

            List<Chart> chartList = boardChartRelMapper.getChartsByBoardId(boardId);
            if (CollectionUtils.isNotEmpty(chartList)) {
                List<ChartDto> chartDtoList = new ArrayList<ChartDto>();
                for (Chart chart : chartList) {
                    ChartDto dto = new ChartDto();
                    BeanUtils.copyProperties(chart, dto);
                    chartDtoList.add(dto);
                }
                boardDto.setChartList(chartDtoList);
            }
        }
        return boardDto;
    }
    @Transactional
    public BaseResult<String> updateBoard(BoardDto boardDto) {
        Board board=new Board();
        BeanUtils.copyProperties(boardDto, board);
        int i=  boardMapper.updateByPrimaryKey(board);
        if(i>0){
            boardChartRelMapper.delete(boardDto.getBoardId());
            List<ChartDto> chartDtos=  boardDto.getChartList();
            if(CollectionUtils.isNotEmpty(chartDtos)){
                for(ChartDto chartDto: chartDtos) {
                    BoardChartRel boardChartRel = new BoardChartRel();
                    boardChartRel.setBoardId(boardDto.getBoardId());
                    boardChartRel.setChartId(chartDto.getChartId());
                    boardChartRel.setCreatedAt(new Date());
                    boardChartRelMapper.insert(boardChartRel);
                }
            }
        }
        return new BaseResult<>(i+"");
    }
    @Transactional
    public BaseResult<String> delBoard(String boardId) {
         int i=  boardMapper.deleteByBoardId(boardId);
         if(i>0){
            int j= boardChartRelMapper.delete(boardId);
            return new BaseResult<>(i+"");
         }
         return new BaseResult<>(0,0+"");

    }
}
