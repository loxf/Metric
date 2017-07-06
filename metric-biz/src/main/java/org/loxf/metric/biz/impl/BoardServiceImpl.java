package org.loxf.metric.biz.impl;


import com.alibaba.dubbo.common.utils.CollectionUtils;
import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.biz.base.BaseService;
import org.loxf.metric.client.BoardService;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.BoardDto;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.dal.po.Board;
import org.loxf.metric.service.BoardManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 看板配置业务类
 * Created by caiyang on 2017/5/4.
 */
@Service("boardService")
public class BoardServiceImpl extends BaseService implements BoardService{
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private BoardManager boardManager;

    /**
     * 获取看板列表支持分页
     * @param boardDto
     * @return
     */
    public PageData listBoradPage(BoardDto boardDto){
        Board board=new Board();
        BeanUtils.copyProperties(boardDto, board);
        PageData pageUtilsUI= null;// super.pageList(board, BoardMapper.class,"Board");
        List<Board> boardList=    pageUtilsUI.getRows();
        List<BoardDto> boardDtoList=new ArrayList<>();
        if(!CollectionUtils.isEmpty(boardList)){
            for(Board b:boardList){
                BoardDto dto=new BoardDto();
                BeanUtils.copyProperties(b, dto);
                boardDtoList.add(dto);
            }
        }
        pageUtilsUI.setRows(boardDtoList);
        return  pageUtilsUI;
    }

    @Override
    public BaseResult<String> createBoard(BoardDto boardDto) {
        try {
            return new BaseResult<String>(boardManager.insert(boardDto));
        } catch (Exception e){
            logger.error("创建看板失败", e);
            throw new MetricException("创建看板失败", e);
        }
    }

    @Override
    public BaseResult<BoardDto> getBoardByBoardId(String boardId) {
        try {
            return new BaseResult(boardManager.getBoardByBoardId(boardId));
        } catch (Exception e){
            logger.error("获取看板失败" + boardId, e);
            throw new MetricException("获取看板失败" + boardId, e);
        }
    }

    @Override
    public BaseResult<String> updateBoard(BoardDto boardDto) {
        return boardManager.updateBoard(boardDto);
    }

    @Override
    public BaseResult<String> delBoard(String boardId) {
        return boardManager.delBoard(boardId);
    }


}
