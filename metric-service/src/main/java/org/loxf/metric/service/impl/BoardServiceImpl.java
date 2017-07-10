package org.loxf.metric.service.impl;


import com.alibaba.dubbo.common.utils.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.api.IBoardService;
import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.service.base.BaseService;
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
import java.util.Map;

/**
 * 看板配置业务类
 * Created by caiyang on 2017/5/4.
 */
@Service("boardService")
public class BoardServiceImpl extends BaseService implements IBoardService {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private BoardManager boardManager;

    /**
     * 获取看板列表支持分页
     * @param boardDto
     * @return
     */
    public PageData getPageList(BoardDto boardDto){
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
    public BaseResult<String> insertIterm(BoardDto boardDto) {
        try {
            return new BaseResult<String>(boardManager.insert(boardDto));
        } catch (Exception e){
            logger.error("创建看板失败", e);
            throw new MetricException("创建看板失败", e);
        }
    }

    @Override
    public BaseResult<BoardDto> queryItemByCode(String boardCode) {
        Map<String,Object> qryParams=new HashedMap();
        qryParams.put("boardCode",boardCode);
        return new BaseResult(boardManager.getBoardDtoByParams(qryParams));
    }

    @Override
    public BaseResult<String> updateItem(String boardCode,Map<String, Object> setParam) {
        try {
            boardManager.updateBoard(boardCode,setParam);
        } catch (Exception e){
            logger.error("更新看板失败!boardCode={}" + boardCode, e);
            throw new MetricException("更新看板失败!boardCode={}" + boardCode, e);
        }
        return new BaseResult(boardCode);
    }

    @Override
    public BaseResult<String> delItemByCode(String boardCode) {
        try {
            boardManager.delBoardByCode(boardCode);
        } catch (Exception e){
            logger.error("更新看板失败!boardCode={}" + boardCode, e);
            throw new MetricException("更新看板失败!boardCode={}" + boardCode, e);
        }
        return new BaseResult(boardCode);
    }


}
