package org.loxf.metric.dal.dao.impl;

import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.DateUtil;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.BoardDao;
import org.loxf.metric.dal.po.Board;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/4.
 */
@Service("boardDao")
public class BoardDaoImpl extends MongoDaoBase<Board> implements BoardDao {
    private static String board_prefix = "BOARDD_";
    private final String collectionName = CollectionConstants.BOARD.name();

    @Override
    public String insert(Board board) {
        String sid = IdGenerator.generate(board_prefix);
        board.setBoardCode(sid);
        board.handleDateToMongo();
        super.insert(board, collectionName);
        return sid;
    }

    @Override
    public Board findOne(Map<String, Object> params) {
        Board board= super.findOne(params, collectionName);
        board.handleMongoDateToJava();
        return board;
    }

    private void handleDateForList(List<Board> list){
        for(Board board:list){
            board.handleMongoDateToJava();
        }
    }
    @Override
    public List<Board> findAll(Map<String, Object> params) {
        List<Board> boardList=super.findAll(params, collectionName);
        handleDateForList(boardList);
        return boardList;
    }

    @Override
    public List<Board> findByPager(Map<String, Object> params, int start, int pageSize) {
        List<Board> boardList=super.findByPager(params, start, pageSize, collectionName);
        handleDateForList(boardList);
        return boardList;
    }

    @Override
    public void update(Map<String, Object> queryParams, Map<String, Object> setParams) {
        super.update(queryParams, setParams, collectionName);
    }

    @Override
    public void updateOne(Map<String, Object> queryParams, Map<String, Object> setParams) {
        super.updateOne(queryParams, setParams, collectionName);
    }

    @Override
    public void remove(Map<String, Object> params) {
        super.remove(params, collectionName);
    }

    @Override
    public long countByParams(Map<String, Object> params) {
        return super.countByParams(params,collectionName);
    }

}
