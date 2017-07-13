package org.loxf.metric.dal.dao.impl;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.utils.DateUtil;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.BoardDao;
import org.loxf.metric.dal.po.Board;
import org.loxf.metric.dal.po.Chart;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/4.
 */
@Service("boardDao")
public class BoardDaoImpl extends MongoDaoBase<Board> implements BoardDao {
    private static String board_prefix = "BOARD_";
    private final String collectionName = CollectionConstants.BOARD.getCollectionName();

    @Override
    public String insert(Board board) {
        String sid = IdGenerator.generate(board_prefix);
        board.setBoardCode(sid);
        super.insert(board, collectionName);
        return sid;
    }

    @Override
    public Board findOne(Board object) {
        Board board= super.findOne(getCommonQuery(object), collectionName);
        return board;
    }

    @Override
    public List<Board> findAll(Board object) {
        List<Board> boardList=super.findAll(getCommonQuery(object), collectionName);
        return boardList;
    }

    @Override
    public List<Board> findByPager(Board object, int start, int pageSize) {
        List<Board> boardList=super.findByPager(getCommonQuery(object), start, pageSize, collectionName);
        return boardList;
    }

    @Override
    public void update(Board object, Map<String, Object> setParams) {
        super.update(getCommonQuery(object), setParams, collectionName);
    }

    @Override
    public void updateOne(String itemCode, Map<String, Object> setParams) {
        Map<String, Object> queryParams=new HashedMap();
        queryParams.put("boardCode",itemCode);
        super.updateOne(queryParams, setParams, collectionName);
    }

    @Override
    public void remove(String itemCode) {
        Map<String, Object> params=new HashedMap();
        params.put("boardCode",itemCode);
        super.remove(params, collectionName);
    }

    @Override
    public long countByParams(Board object) {
        return super.countByParams(getCommonQuery(object),collectionName);
    }

    private Query getCommonQuery(Board board){
        //TODO 实现各个dao自己的query
        return null;
    }
}
