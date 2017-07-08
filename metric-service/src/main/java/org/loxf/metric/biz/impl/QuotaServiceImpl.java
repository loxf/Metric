package org.loxf.metric.biz.impl;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.api.IQuotaService;
import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.biz.Operation.Expression;
import org.loxf.metric.biz.base.TableData;
import org.loxf.metric.biz.base.TableDim;
import org.loxf.metric.biz.utils.*;
import org.loxf.metric.client.QuotaService;
import org.loxf.metric.common.constants.ChartTypeEnum;
import org.loxf.metric.common.constants.QuotaType;
import org.loxf.metric.common.dto.*;
import org.loxf.metric.dal.po.Quota;
import org.loxf.metric.service.ChartManager;
import org.loxf.metric.service.QuotaManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("quotaService")
public class QuotaServiceImpl implements IQuotaService {
    private static Logger logger = LoggerFactory.getLogger(QuotaServiceImpl.class);
    @Autowired
    private QuotaManager quotaManager;
    @Autowired
    private ValidConditionUtil valid;
    @Autowired
    private QuotaOperation operation;
    @Autowired
    private ChartManager chartManager;

    public BaseResult<String> insertIterm(QuotaDto quotaDto) {
        try {
            // 获取表达式的指标
            if(StringUtils.isEmpty(quotaDto.getExpression())){
                throw new MetricException("表达式不能为空");
            }
            String expression = quotaDto.getExpression();
            List<String> quotaCodeList = QuotaSqlBuilder.quotaList(expression);
            if(quotaDto.getType().equals(QuotaType.COMPOSITE.getValue()) && CollectionUtils.isEmpty(quotaCodeList)){
                throw new MetricException("复合指标必须基于其他指标运算");
            } else if(quotaDto.getType().equals(QuotaType.COMPOSITE.getValue())){
                // 复合指标复合复合指标时，需要把运算公式换算为基础指标
                for(String quotaCode : quotaCodeList){
                    Map map=new HashedMap();
                    map.put("quotaCode",quotaCode);
                    Quota tmp = quotaManager.getQuotaByParams(map);
                    if(tmp.getType().equals(QuotaType.COMPOSITE.getValue())){
                        // 换算为基础指标
                        expression = expression.replace("${" + quotaCode + "}", "(" + tmp.getExpression() + ")");
                    }
                }
                quotaDto.setExpression(expression);
            }
            return new BaseResult(quotaManager.insert(quotaDto));
        } catch (Exception e) {
            logger.error("创建指标失败", e);
            throw new MetricException("创建指标失败", e);
        }
    }

    @Override
    public BaseResult<String> updateItem(String quotaCode,Map<String, Object> setParams) {
//        try {
//            // 获取表达式的指标
//            if(StringUtils.isEmpty(quotaDto.getQuotaId())){
//                throw new MetricException("指标ID不能为空");
//            }
//            if(StringUtils.isEmpty(quotaDto.getType())){
//                throw new MetricException("指标类型不能为空");
//            }
//            List<String> quotaList = null;
//            String expression = quotaDto.getExpression();
//            if(StringUtils.isNotEmpty(expression)) {
//                quotaList = QuotaSqlBuilder.quotaList(quotaDto.getExpression());
//                List<String> quotaCodeList = QuotaSqlBuilder.quotaList(expression);
//                if(quotaDto.getType().equals(QuotaType.COMPOSITE.getValue()) && CollectionUtils.isEmpty(quotaCodeList)){
//                    throw new MetricException("复合指标必须基于其他指标运算");
//                } else if(quotaDto.getType().equals(QuotaType.COMPOSITE.getValue())){
//                    // 复合指标复合复合指标时，需要把运算公式换算为基础指标
//                    for(String quotaCode : quotaCodeList){
//                        Quota tmp = quotaManager.getQuotaByCode(quotaCode);
//                        if(tmp.getType().equals(QuotaType.COMPOSITE.getValue())){
//                            // 换算为基础指标
//                            expression = expression.replace("${" + quotaCode + "}", "(" + tmp.getExpression() + ")");
//                        }
//                    }
//                    quotaDto.setExpression(expression);
//                }
//            }
//            return new BaseResult(quotaManager.updateQuota(quotaDto, quotaList));
//        } catch (Exception e) {
//            logger.error("更新指标失败", e);
//            throw new MetricException("更新指标失败", e);
//        }
        return null;
    }

    @Override
    public BaseResult<String> delItemByCode(String code) {
         quotaManager.delQuotaByCode(code);
         return new BaseResult<>(code);
    }

    @Override
    public BaseResult<QuotaDto> queryItemByCode(String quotaId){
        Quota quota = quotaManager.getQuotaById(quotaId);
        QuotaDto dto = new QuotaDto();
        BeanUtils.copyProperties(quota, dto);
        return new BaseResult(dto);
    }

//    @Override
//    public BaseResult getQuotaData(String scanId, ConditionVo condition){
//        /*QuotaScan quotaScan = quotaScanManager.getQuotaScanById(scanId);
//        if(quotaScan==null){
//            return new BaseResult<>(BaseResult.FAILED, "无此指标概览");
//        }
//        Quota quota = quotaManager.getQuotaById(quotaScan.getQuotaId());
//        if(quota==null){
//            return new BaseResult<>(BaseResult.FAILED, "指标概览对应的指标不存在");
//        }
//        condition.setQuotaCode(quota.getQuotaCode());
//        List<GroupBy> groupBy = new ArrayList<>();
//        String quotaDim = quotaScan.getQuotaDim();
//        if(StringUtils.isNotEmpty(quotaDim)){
//            String [] tmp = quotaDim.split(",");
//            for(String s : tmp){
//                groupBy.add(new GroupBy(s));
//            }
//        }
//        condition.setGroupBy(groupBy);
//        // 条件校验
//        valid.valid(quota.getQuotaCode(), condition);
//        // 获取指标值
//        Expression expression = new Expression(quota.getExpression());
//        expression.init(condition);
//        List<QuotaData> mapList = expression.execute();
//        //
//        String oper = StringUtils.isEmpty(quota.getShowOperation())? "" : quota.getShowOperation();
//        // 根据指标类型处理值
//        if (CollectionUtils.isNotEmpty(mapList) && mapList.get(0) != null) {
//            List<Command> commands = CommandUtils.getCommands(oper, true);
//            // 根据指标类型处理值
//            if("NUM".equals(quota.getShowType())){
//                commands.add(new Command("R", "0"));
//            } else if("MONEY".equals(quota.getShowType())){
//                commands.add(new Command("R", "2"));
//            } else if("PERCENT".equals(quota.getShowType())){
//                commands.add(new Command("MULTIPLY", "100"));
//                commands.add(new Command("R", "2"));
//            }
//            Object tmp = mapList;
//            if(CollectionUtils.isNotEmpty(commands)) {
//                for (Command cmd : commands) {
//                    tmp = CommandUtils.executeCommand(cmd, tmp);
//                }
//            }
//            return new BaseResult(tmp.toString());
//        }*/
//        return new BaseResult("0");
//    }
//
//    @Override
//    public ChartData getChartData(String chartId, String summaryOperation, ConditionVo condition){
//        ChartDto chart = chartManager.getChart(chartId);
//        String startDateStr = condition.getStartCircleTime();
//        String endDateStr = condition.getEndCircleTime();
//        ChartData result = new ChartData();
//        List<QuotaDto> quotaList = chart.getQuotaList();
//        List cols = new ArrayList();
//        boolean colsInit = false;
//        List quotaDataInfo = new ArrayList();
//
//        if(quotaList!=null){
//            if(ChartTypeEnum.TABLE.equals(chart.getType())){
//                // table 比较特殊 需要特殊处理
//                TableData tableData = null;
//                // 遍历指标 生成列 code:name 获取数据
//                List<String> colCodes = new ArrayList<>();
//                // 获取图维度(表格格式时 各指标维度一致)
//                ArrayList<GroupBy> chartGroupBy = new ArrayList<>();
//                // 设置维度信息
//                String dimStr = quotaList.get(0).getChartDimension();// 图表维度 必须有维度
//                if (StringUtils.isEmpty(dimStr)) {
//                    dimStr = "CIRCLE_TIME";
//                }
//                String[] tmpArr = dimStr.split(",");
//                tableData = new TableData(tmpArr);
//                for (String s : tmpArr) {
//                    String str = s + ":" + quotaManager.selectDimByDimCode(quotaList.get(0).getQuotaId(), s);
//                    if("CIRCLE_TIME".equals(s)){
//                        str = str + ":DATE";
//                    } else {
//                        str = str + ":TEXT";
//                    }
//                    cols.add(str);
//                    colCodes.add(s);
//                    chartGroupBy.add(new GroupBy(s));
//                }
//                condition.setGroupBy(chartGroupBy);
//
//                for (QuotaDto quotaDto : quotaList) {
//                    condition.setQuotaCode(quotaDto.getQuotaCode());
//                    // 条件校验
//                    try {
//                        valid.valid(quotaDto.getQuotaCode(), condition);
//                    } catch (MetricException e) {
//                        logger.warn("当前指标无此条件，查询结果为空", e);
//                        if ("CHECK_CONDITION_01".equals(e.getCode())) {
//                            return result;
//                        }
//                    }
//                    // 获取数据
//                    Expression expression = new Expression(quotaDto.getExpression(), false, summaryOperation);
//                    expression.init(condition);
//                    List<QuotaData> mapList = expression.execute();
//                    // 运算
//                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(mapList)) {
//                        String oper = quotaDto.getShowOperation();
//                        List<Command> commands = CommandUtils.getCommands(oper, false);
//                        // 根据指标类型处理值
//                        if("NUM".equals(quotaDto.getShowType())){
//                            commands.add(new Command("R", "0"));
//                        } else if("MONEY".equals(quotaDto.getShowType())){
//                            commands.add(new Command("R", "2"));
//                        } else if("PERCENT".equals(quotaDto.getShowType())){
//                            commands.add(new Command("MULTIPLY", "100"));
//                            commands.add(new Command("R", "2"));
//                        }
//                        if (CollectionUtils.isNotEmpty(commands)) {
//                            for (Command cmd : commands) {
//                                mapList = (List<QuotaData>) CommandUtils.executeCommand(cmd, mapList);
//                            }
//                        }
//                    }
//                    for(QuotaData tmp : mapList){
//                        // 处理数据
//                        TableDim dim = new TableDim();
//                        for(GroupBy gb : chartGroupBy) {
//                            String gbValue = "";
//                            if(gb.getCode().equals("CIRCLE_TIME")) {
//                                gbValue = CircleTimeUtil.formatCircleTimeByParticleSize((tmp.getDimValue(gb.getCode())), quotaDto.getParticleSize());
//                            } else {
//                                gbValue = tmp.getDimValue(gb.getCode());
//                            }
//                            dim.add(gbValue);
//                        }
//                        tableData.addValue(dim, quotaDto.getQuotaCode(), tmp.getValue()==null?"0":tmp.getValue().toString());
//                    }
//
//                    // 设置列名 计算汇总
//                    String summaryOper = quotaDto.getShowOperation().split(" ")[0];
//                    String summaryStr = getNameByOper(summaryOper) ;
//                    if(CollectionUtils.isNotEmpty(mapList)) {
//                        Command command = new Command(summaryOper, "value");
//                        String summaryValue = CommandUtils.executeCommand(command, mapList).toString();
//                        summaryStr = summaryStr + "[" + summaryValue.toString() + "]";
//                    } else {
//                        summaryStr = summaryStr + "[ 0 ]";
//                    }
//                    cols.add(quotaDto.getQuotaCode() + ":" + quotaDto.getQuotaDisplayName() +
//                            ":" + ("NUM".equalsIgnoreCase(quotaDto.getShowType())?"INT":"FLOAT") + ":" + summaryStr);
//                    colCodes.add(quotaDto.getQuotaCode());
//                }
//                result.setData(tableData.getValues());
//            } else {
//                for (QuotaDto quotaDto : quotaList) {
//                    Date startDate = CircleTimeUtil.formatStrToDate(startDateStr, quotaDto.getParticleSize());
//                    Date endDate = CircleTimeUtil.formatStrToDate(endDateStr, quotaDto.getParticleSize());
//                    condition.setQuotaCode(quotaDto.getQuotaCode());
//                    ArrayList<GroupBy> chartGroupBy = new ArrayList<>();
//                    // 获取图维度
//                    String dimStr = quotaDto.getChartDimension();// 图表维度 必须有维度
//                    if (StringUtils.isNotEmpty(dimStr)) {
//                        String[] tmp = dimStr.split(",");
//                        for (String s : tmp) {
//                            chartGroupBy.add(new GroupBy(s));
//                        }
//                    }
//                    condition.setGroupBy(chartGroupBy);
//                    try {
//                        valid.valid(quotaDto.getQuotaCode(), condition);
//                    } catch (MetricException e) {
//                        logger.warn("当前指标无此条件，查询结果为空", e);
//                        if ("CHECK_CONDITION_01".equals(e.getCode())) {
//                            return result;
//                        }
//                    }
//                    Expression expression = new Expression(quotaDto.getExpression(), false, null);
//                    expression.init(condition);
//                    List<QuotaData> mapList = expression.execute();
//                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(mapList)) {
//                        String oper = quotaDto.getShowOperation();
//                        List<Command> commands = CommandUtils.getCommands(oper, false);
//                        // 根据指标类型处理值
//                        if("NUM".equals(quotaDto.getShowType())){
//                            commands.add(new Command("R", "0"));
//                        } else if("MONEY".equals(quotaDto.getShowType())){
//                            commands.add(new Command("R", "2"));
//                        } else if("PERCENT".equals(quotaDto.getShowType())){
//                            commands.add(new Command("MULTIPLY", "100"));
//                            commands.add(new Command("R", "2"));
//                        }
//                        if (CollectionUtils.isNotEmpty(commands)) {
//                            for (Command cmd : commands) {
//                                mapList = (List<QuotaData>) CommandUtils.executeCommand(cmd, mapList);
//                            }
//                        }
//                        if (ChartTypeEnum.PILE.equals(chart.getType()) ||
//                                ChartTypeEnum.BROKENLINE.equals(chart.getType())) {
//                            List data = new ArrayList();
//                            // 如果是折线，先生成所有的日期列并初始化值
//                            if (dimStr.equals("CIRCLE_TIME")) {
//                                while (startDate.getTime() <= endDate.getTime()) {
//                                    data.add("0");
//                                    if (!colsInit) {
//                                        cols.add(CircleTimeUtil.formatCircleTimeByParticleSize(startDate, quotaDto.getParticleSize()));
//                                    }
//                                    long nextTime = CircleTimeUtil.getNextCircleTime(startDate, quotaDto.getParticleSize());
//                                    startDate.setTime(nextTime);
//                                }
//                                colsInit = true;
//                            }
//                            for (QuotaData map : mapList) {
//                                if (map == null) {
//                                    continue;
//                                }
//                                String dimValue = map.getDimValue(dimStr);
//                                if (dimStr.equals("CIRCLE_TIME")) {
//                                    String timeStr = CircleTimeUtil.formatCircleTimeByParticleSize(dimValue, quotaDto.getParticleSize());
//                                    // 从开始时间到结束时间遍历 如果遇到相等插入值，如果遇到不相等设置值为0
//                                    int i = 0;
//                                    for (String col : (List<String>) cols) {
//                                        if (timeStr.equals(col)) {
//                                            Object value = map.getValue().toString();
//                                            data.set(i, value);
//                                            break;
//                                        }
//                                        i++;
//                                    }
//                                } else {
//                                    cols.add(dimValue);
//                                    Object value = map.getValue().toString();
//                                    data.add(value);
//                                }
//                            }
//                            Map quotaResult = new HashMap();
//                            quotaResult.put("name", quotaDto.getQuotaName());
//                            quotaResult.put("type", "line");
//                            if (ChartTypeEnum.PILE.equals(chart.getType())) {
//                                quotaResult.put("stack", "总量");// 写死
//                                //  "areaStyle": {"normal": {}}, 写死
//                                Map normal = new HashMap();
//                                normal.put("normal", new HashMap<>());
//                                quotaResult.put("areaStyle", normal);
//                            }
//                            quotaResult.put("data", data);
//                            quotaDataInfo.add(quotaResult);
//                        } else if (ChartTypeEnum.ROUND.equals(chart.getType())) {
//                            for (QuotaData map : mapList) {
//                                if (map == null) {
//                                    continue;
//                                }
//                                String dimValue = map.getDimValue(dimStr);
//                                if (dimStr.equals("CIRCLE_TIME")) {
//                                    dimValue = CircleTimeUtil.formatCircleTimeByParticleSize(dimValue, quotaDto.getParticleSize());
//                                }
//                                cols.add(dimValue);
//                                Map rowData = new HashMap();
//                                Object value = map.getValue().toString();
//                                rowData.put("value", value);
//                                rowData.put("name", dimValue);
//                                quotaDataInfo.add(rowData);
//                            }
//                        } else {
//                            for (QuotaData map : mapList) {
//                                if (map == null) {
//                                    continue;
//                                }
//                                String dimValue = map.getDimValue(dimStr) ;
//                                if (dimStr.equals("CIRCLE_TIME")) {
//                                    cols.add(CircleTimeUtil.formatCircleTimeByParticleSize(dimValue, quotaDto.getParticleSize()));
//                                } else {
//                                    cols.add(dimValue);
//                                }
//                                Object value = map.getValue().toString();
//                                quotaDataInfo.add(value);
//                            }
//                        }
//                    }
//                }
//                result.setData(quotaDataInfo);
//            }
//            result.setCols(cols);
//        }
//        return result;
//    }
//
//    @Override
//    public BaseResult<List<QuotaDto>> queryQuotaNameAndId(QuotaDto quotaDto) {
//        return quotaManager.queryQuotaNameAndId(quotaDto);
//    }
//
//    @Override
//    public List<QuotaDimensionDto> queryDimenListByChartId(String chartId){
//        return quotaManager.queryDimenListByChartId(chartId);
//    }
//
//    @Override
//    public List<QuotaDimensionDto> queryDimenListByBoardId(String boardId){
//        return quotaManager.queryDimenListByBoardId(boardId);
//    }
//
//    @Override
//    public List<QuotaDimensionDto> queryDimenListByQuotaId(String quotaId){
//        return quotaManager.queryDimenListByQuotaId(quotaId);
//    }
//
//    @Override
//    public List<Map> queryDimenListByQuotaCode(String[] quotaCodes){
//        return quotaManager.queryDimenListByQuotaCode(quotaCodes);
//    }
//
    @Override
    public PageData<QuotaDto> getPageList(QuotaDto quotaDto) {
        try {
            PageData<QuotaDto> list = quotaManager.listQuotaPage(quotaDto);
            return list;
        } catch (Exception e){
            logger.error("分页获取指标：" + e.getMessage(), e);
            throw new MetricException("分页获取指标：" + e.getMessage(), e);
        }
//    }
//
//    @Override
//    public BaseResult<QuotaDto> getQuotaByCode(String quotaCode) {
//        Quota quota = quotaManager.getQuotaByCode(quotaCode);
//        QuotaDto dto = new QuotaDto();
//        if(quota !=null){
//            BeanUtils.copyProperties(quota, dto);
//        }
//        return new BaseResult(dto);
//    }
//
//    private String getNameByOper(String oper){
//        if("SUM".equalsIgnoreCase(oper)){
//            return "合计";
//        } else if("AVG".equalsIgnoreCase(oper)){
//            return "平均";
//        } else if("MAX".equalsIgnoreCase(oper)){
//            return "最大";
//        } else if("MIN".equalsIgnoreCase(oper)){
//            return "最小";
//        } else if("COUNT".equalsIgnoreCase(oper)){
//            return "数量";
//        } else {
//            return oper;
//        }
//    }
}
