package org.loxf.metric.dal.dao.impl;

import com.mongodb.BasicDBObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.base.constants.CollectionConstants;
import org.loxf.metric.base.constants.StandardState;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.core.mongo.MongoDaoBase;
import org.loxf.metric.dal.dao.interfaces.BoardDao;
import org.loxf.metric.dal.po.Board;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
        setParams.put("updatedAt",new Date());
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
        BasicDBObject query = new BasicDBObject();
        if(StringUtils.isNotEmpty(board.getBoardCode())){
            query.put("boardCode", board.getBoardCode());
        }
        if(StringUtils.isNotEmpty(board.getBoardName())){//模糊匹配
            Pattern pattern = Pattern.compile("^.*" + board.getBoardName() +".*$", Pattern.CASE_INSENSITIVE);
            query.put("boardName", pattern);
        }

        if(StringUtils.isNotEmpty(board.getUniqueCode())){
            query.put("uniqueCode", board.getUniqueCode());
        }

        if(StringUtils.isNotEmpty(board.getCreateUserName())){
            query.put("createUserName", board.getCreateUserName());
        }

//        List<ChartItem> chartItemList=board.getChartList();
//        if(chartItemList!=null&&chartItemList.size()>0){
//
//        }

        if(board.getStartDate()!=null||board.getEndDate()!=null){
            Map<String, Object> createT = new HashMap<>();
            if(board.getStartDate()!=null)
                createT.put("$gte", board.getStartDate());
            if(board.getEndDate()!=null)
                createT.put("$lte", board.getEndDate());
            query.put("createdAt", createT);
        }
        query.put("state",StandardState.AVAILABLE.getValue());
        return new BasicQuery(query);
    }

    @Override
    public Board findByBoardCode(String boardCode) {
        Map qryParams=new HashedMap();
        qryParams.put("boardCode",boardCode);
        qryParams.put("state", StandardState.AVAILABLE.getValue());
        return super.findOne(qryParams,collectionName);
    }
}
