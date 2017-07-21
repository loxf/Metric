package org.loxf.metric.service.impl;

import com.alibaba.druid.sql.visitor.functions.Char;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.api.IBoardService;
import org.loxf.metric.base.ItemList.ChartItem;
import org.loxf.metric.base.ItemList.VisibleItem;
import org.loxf.metric.base.constants.ComPareConstants;
import org.loxf.metric.base.constants.VisibleTypeEnum;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.base.constants.StandardState;
import org.loxf.metric.common.constants.UserTypeEnum;
import org.loxf.metric.common.dto.*;
import org.loxf.metric.base.utils.MapAndBeanTransUtils;
import org.loxf.metric.dal.dao.interfaces.BoardDao;
import org.loxf.metric.dal.dao.interfaces.ChartDao;
import org.loxf.metric.dal.dao.interfaces.UserDao;
import org.loxf.metric.dal.po.Board;
import org.apache.log4j.Logger;
import org.loxf.metric.dal.po.Chart;
import org.loxf.metric.dal.po.Target;
import org.loxf.metric.dal.po.User;
import org.loxf.metric.service.base.BaseService;
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
    private BoardDao boardDao;

    @Autowired
    private ChartDao chartDao;

    @Autowired
    private UserDao userDao;

    @Override
    public BaseResult<String> insertItem(BoardDto obj) {
        BaseResult result = new BaseResult();
        if (StringUtils.isEmpty(obj.getBoardName()) || StringUtils.isEmpty(obj.getUniqueCode()) ||
                StringUtils.isEmpty(obj.getHandleUserName()) || StringUtils.isEmpty(obj.getUniqueCode())) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }

        Board board = new Board();
        BeanUtils.copyProperties(obj, board);
        board.setState(StandardState.AVAILABLE.getValue());
        board.setCreateUserName(obj.getHandleUserName());
        return new BaseResult<>(boardDao.insert(board));
    }

    @Override
    public BaseResult<PageData<BoardDto>> getPageList(BoardDto obj) {

        Pager pager=obj.getPager();
        BaseResult result=super.validPager(pager);
        if(!ResultCodeEnum.SUCCESS.getCodeMsg().equals(result.getCode())){
            return result;
        }
        if(StringUtils.isEmpty(obj.getUniqueCode())){//只能查询所属团队的看板
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        obj.setState(StandardState.AVAILABLE.getValue());
        Map<String, Object> params = MapAndBeanTransUtils.transBean2Map(obj);
        result.setData(getPageResult(boardDao, params, pager.getStart(), pager.getRownum()));
        return result;
    }


    @Override
    public BaseResult<BoardDto> queryItemByCode(String itemCode, String handleUserName) {
        BaseResult<BoardDto> result = new BaseResult<>();
        if (StringUtils.isEmpty(itemCode)) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        User user = new User();
        user.setUserName(handleUserName);
        user = userDao.findOne(user);
        if(user!=null) {
            Board qryParams = new Board();
            qryParams.setBoardCode(itemCode);
            Board board = boardDao.findOne(qryParams);
            if(UserTypeEnum.CHILD.name().equals(user.getUserType())){
                //获取该用户可见范围内的图
                List<ChartItem> hasPermissionChart = board.getChartList();
                List<ChartItem> chartItemList = board.getChartList();
                if(CollectionUtils.isNotEmpty(chartItemList)) {
                    Chart chart = new Chart();
                    chart.setUniqueCode(user.getUniqueCode());
                    for(ChartItem chartItem : chartItemList) {
                        chart.setChartCode(chartItem.getChartCode());
                        long count = chartDao.countByParams(chart, handleUserName);
                        if (count>0) {
                            hasPermissionChart.add(chartItem);
                        }
                    }
                }
                board.setChartList(hasPermissionChart);
            }
            BoardDto boardDto = new BoardDto();
            BeanUtils.copyProperties(board, boardDto);//前者赋值给后者
            result.setData(boardDto);
        } else {
            result.setCode(ResultCodeEnum.USER_NOT_EXIST.getCode());
            result.setMsg(ResultCodeEnum.USER_NOT_EXIST.getCodeMsg());
        }
        return result;
    }

    @Override
    public BaseResult<String> updateItem(BoardDto obj) {
        BaseResult<String> result = new BaseResult<>();
        String itemCode = obj.getBoardCode();
        if (StringUtils.isEmpty(itemCode)) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg("boardCode不能为空！");
            return result;
        }
        obj.setUpdateUserName(obj.getHandleUserName());
        Board paramBoard=new Board();
        BeanUtils.copyProperties(obj, paramBoard);
        paramBoard.setUpdateUserName(obj.getHandleUserName());
        Map<String,Object> boardMap = MapAndBeanTransUtils.transBean2Map(paramBoard);
        StringBuilder updateParams=new StringBuilder();
        for (Map.Entry<String,Object> entry : boardMap.entrySet()) {
            updateParams.append( entry.getKey()+":"+entry.getValue()+";");
        }
        logger.info(obj.getHandleUserName()+"将更新boardCode="+itemCode+"的数据为"+updateParams);
        boardDao.updateOne(itemCode, boardMap);
        return result;
    }

    @Override
    public BaseResult<String> delItemByCode(String itemCode, String handleUserName) {
        logger.info("客户:" + handleUserName + "将要删除看板,itemCode" + itemCode);
        BaseResult<String> result = new BaseResult<>();
        if (StringUtils.isEmpty(itemCode)) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg("看板code不能为空!");
            return result;
        }
        Map chartMap = new HashedMap();
        chartMap.put("state", StandardState.DISABLED.getValue());
        chartMap.put("updateUserName", handleUserName);
        boardDao.updateOne(itemCode, chartMap);
        return result;
    }

    @Override
    public BaseResult<String> addChart(String boardCode,String handleUserName,List<ChartItem> chartItemList) {
        BaseResult<String> result=new BaseResult<>();
        if(StringUtils.isBlank(boardCode)){
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setCode("看板code缺失,无法添加!");
            return result;
        }
        if(chartItemList==null||chartItemList.size()==0){
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setCode("请添加图!");
            return result;
        }
        Board board=boardDao.findByBoardCode(boardCode);
        if(board==null){
            result.setCode(ResultCodeEnum.DATA_NOT_EXIST.getCode());
            result.setCode("不存在该看板!");
            return result;
        }
        boolean validChartFlag=true;
        boolean checkEmptyChartFlag=true;
        for(ChartItem chartItem:chartItemList){
            if(StringUtils.isBlank(chartItem.getChartCode())||StringUtils.isBlank(chartItem.getChartName())||StringUtils.isBlank(chartItem.getProperties())){
                checkEmptyChartFlag=false;
                break;
            }else if(chartDao.findByCode(chartItem.getChartCode(),handleUserName)==null){
                validChartFlag=false;
                break;
            }
        }
        if(!checkEmptyChartFlag){
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setCode("图信息缺失,无法添加!");
            return result;
        }
        if(!validChartFlag){
            result.setCode(ResultCodeEnum.DATA_NOT_EXIST.getCode());
            result.setCode("图不存在,无法添加！");
            return result;
        }

        List<ChartItem> boardChartItemList=board.getChartList();
        boardChartItemList.addAll(chartItemList);
        Map setMap=new HashedMap();
        setMap.put("chartList",boardChartItemList);
        boardDao.updateOne(boardCode,setMap);
        return result;
    }

    @Override
    public BaseResult<String> delChart(String boardCode,String chartCode, String handleUserName) {
        BaseResult<String> result=new BaseResult<>();
        if(StringUtils.isBlank(chartCode)||StringUtils.isBlank(boardCode)){
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setCode(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        Board board=boardDao.findByBoardCode(boardCode);
        if(board==null){
            result.setCode(ResultCodeEnum.DATA_NOT_EXIST.getCode());
            result.setCode("不存在该看板");
            return result;
        }
        List<ChartItem> chartItemList=board.getChartList();
        boolean flag=false;
        for(ChartItem chartItem:chartItemList){
            String chartItemCode=chartItem.getChartCode();
            if(chartCode.equals(chartItemCode)){
                chartItemList.remove(chartItem);
                break;
            }
        }
        if(flag){//存在则删除
            Map setMap=new HashedMap();
            setMap.put("chartList",chartItemList);
            boardDao.updateOne(boardCode,setMap);
        }else{
            result.setCode(ResultCodeEnum.DATA_NOT_EXIST.getCode());
            result.setCode("该看板没有配置该图!");
            return result;
        }
        return result;
    }
/*
    @Override
    public BaseResult<List<ChartItem>> getOwnChartList(String boardCode, String handleUserName) {
        BaseResult<List<ChartItem>> result=new BaseResult<List<ChartItem>>();
        if(StringUtils.isBlank(boardCode)){
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setCode(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        Board board=boardDao.findByBoardCode(boardCode);
        if(board==null){
            result.setCode(ResultCodeEnum.DATA_NOT_EXIST.getCode());
            result.setCode("不存在该看板");
            return result;
        }
        result.setData(board.getChartList());
        return  result;
    }*/

    @Override
    public BaseResult<List<ChartItem>> getOtherChartListByPage(BoardDto boardDto) {
        Pager pager=boardDto.getPager();
        BaseResult result=super.validPager(pager);
        if(!ResultCodeEnum.SUCCESS.getCodeMsg().equals(result.getCode())){
            return result;
        }
        if(StringUtils.isBlank(boardDto.getBoardCode())){
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setCode(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }

        Board board=boardDao.findByBoardCode(boardDto.getBoardCode());
        if(board==null){
            result.setCode(ResultCodeEnum.DATA_NOT_EXIST.getCode());
            result.setCode("不存在该看板");
            return result;
        }
        List<ChartItem> chartItemList=board.getChartList();
        List boardChartCodeList=new ArrayList();
        for(ChartItem chartItem:chartItemList){
            boardChartCodeList.add(chartItem.getChartCode());
        }
        Map qryMap=new HashedMap();
        qryMap.put("uniqueCode",board.getUniqueCode());
        Map chartNotInMap=new HashedMap();
        chartNotInMap.put(ComPareConstants.NOTIN.getDisplayName(),boardChartCodeList);
        qryMap.put("chartCode",chartNotInMap);

        PageData pageData=getChartPageResult(qryMap, pager.getStart(), pager.getRownum());
        result.setData(pageData);
        return result;
    }

    public PageData getChartPageResult(Map<String, Object> params, int start, int pageSize) {
        try {
            long totalCount = (long) chartDao.countByParams(params);
            if (totalCount <= 0) {
                return null;
            }
            List pageResult = chartDao.findPagerByParams(params, start, pageSize);
            PageData pageData=new PageData();
            pageData.setRows(pageResult);
            pageData.setTotalRecords(totalCount);
            pageData.setCurrentPage(start/pageSize+1);
            pageData.setRownum(pageSize);
            pageData.setTotalPage(pageData.calculateTotalPage());
            return pageData;
        }catch (Exception e){
            logger.error("查询分页异常！",e);
            return null;
        }
    }

}