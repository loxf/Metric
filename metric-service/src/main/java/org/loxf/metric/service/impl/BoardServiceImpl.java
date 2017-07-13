package org.loxf.metric.service.impl;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.api.IBoardService;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.*;
import org.loxf.metric.base.utils.MapAndBeanTransUtils;
import org.loxf.metric.dal.dao.interfaces.BoardDao;
import org.loxf.metric.dal.po.Board;
import org.apache.log4j.Logger;
import org.loxf.metric.service.base.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Date;
import java.util.Map;

/**
 * 看板配置业务类
 * Created by caiyang on 2017/5/4.
 */
@Service("boardService")
public class BoardServiceImpl extends BaseService implements IBoardService {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private BoardDao boardDao;

    @Override
    public BaseResult<String> insertItem(BoardDto obj) {
            Board board = new Board();
            BeanUtils.copyProperties(obj, board);
            board.setCreatedAt(new Date());
            board.setUpdatedAt(new Date());
            return new BaseResult<>(boardDao.insert(board));
    }

    @Override
    public BaseResult<PageData> getPageList(BoardDto obj){
        return null;
    }

//    @Override
//    public PageData getPageList(BoardDto obj) {
//        Pager pager = obj.getPager();
//        if (pager == null) {
//            logger.info("分页信息为空，无法查询!");
//            return null;
//        }
//        Map<String, Object> params = MapAndBeanTransUtils.transBean2Map(obj);
//
//        List<Board> boardList = boardDao.findByPager(params, pager.getStart(), pager.getRownum());
//        PageData pageData = new PageData();
//        pageData.setTotalRecords(boardList.size());
//        pageData.setRownum(pager.getRownum());
//        pageData.setCurrentPage(pager.getCurrentPage());
//        pageData.setRows(boardList);
//        return pageData;
//    }

    @Override
    public BaseResult<BoardDto> queryItemByCode(String itemCode,String handleUserName) {
        Board qryParams = new Board();
        qryParams.setBoardCode(itemCode);
        Board board = boardDao.findOne(qryParams);
        BoardDto boardDto = new BoardDto();
        BeanUtils.copyProperties(board, boardDto);//前者赋值给后者
        return new BaseResult<>(boardDto);
    }

    @Override
    public BaseResult<String> updateItem(BoardDto obj) {
        String itemCode = obj.getBoardCode();
        if (StringUtils.isEmpty(itemCode)) {
            return new BaseResult<>("boardCode不能为空！");
        }
        Map boardMap = MapAndBeanTransUtils.transBean2Map(obj);
        boardDao.updateOne(itemCode, boardMap);
        return new BaseResult<>();
    }

    @Override
    public BaseResult<String> delItemByCode(String itemCode,String handleUserName) {
        boardDao.remove(itemCode);
        return new BaseResult<>();
    }


}