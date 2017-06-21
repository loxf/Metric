package org.loxf.metric.biz.impl;

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

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by luohj on 2017/5/4.
 */
@Service("quotaService")
public class QuotaServiceImpl implements QuotaService {
    private static Logger logger = LoggerFactory.getLogger(QuotaServiceImpl.class);
    @Autowired
    private QuotaManager quotaManager;
    @Autowired
    private ValidConditionUtil valid;
    @Autowired
    private QuotaOperation operation;
    @Autowired
    private ChartManager chartManager;

    @Override
    public BaseResult<String> createQuota(QuotaDto quotaDto) {
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
                    Quota tmp = quotaManager.getQuotaByCode(quotaCode);
                    if(tmp.getType().equals(QuotaType.COMPOSITE.getValue())){
                        // 换算为基础指标
                        expression = expression.replace("${" + quotaCode + "}", "(" + tmp.getExpression() + ")");
                    }
                }
                quotaDto.setExpression(expression);
            }

            return new BaseResult(quotaManager.createQuota(quotaDto, quotaCodeList));
        } catch (Exception e) {
            logger.error("创建指标失败", e);
            throw new MetricException("创建指标失败", e);
        }
    }

    @Override
    public BaseResult<String> updateQuota(QuotaDto quotaDto) {
        try {
            // 获取表达式的指标
            if(StringUtils.isEmpty(quotaDto.getQuotaId())){
                throw new MetricException("指标ID不能为空");
            }
            if(StringUtils.isEmpty(quotaDto.getType())){
                throw new MetricException("指标类型不能为空");
            }
            List<String> quotaList = null;
            String expression = quotaDto.getExpression();
            if(StringUtils.isNotEmpty(expression)) {
                quotaList = QuotaSqlBuilder.quotaList(quotaDto.getExpression());
                List<String> quotaCodeList = QuotaSqlBuilder.quotaList(expression);
                if(quotaDto.getType().equals(QuotaType.COMPOSITE.getValue()) && CollectionUtils.isEmpty(quotaCodeList)){
                    throw new MetricException("复合指标必须基于其他指标运算");
                } else if(quotaDto.getType().equals(QuotaType.COMPOSITE.getValue())){
                    // 复合指标复合复合指标时，需要把运算公式换算为基础指标
                    for(String quotaCode : quotaCodeList){
                        Quota tmp = quotaManager.getQuotaByCode(quotaCode);
                        if(tmp.getType().equals(QuotaType.COMPOSITE.getValue())){
                            // 换算为基础指标
                            expression = expression.replace("${" + quotaCode + "}", "(" + tmp.getExpression() + ")");
                        }
                    }
                    quotaDto.setExpression(expression);
                }
            }
            return new BaseResult(quotaManager.updateQuota(quotaDto, quotaList));
        } catch (Exception e) {
            logger.error("更新指标失败", e);
            throw new MetricException("更新指标失败", e);
        }
    }

    @Override
    public BaseResult<String> delQuota(QuotaDto quotaDto) {
        return quotaManager.delQuota(quotaDto);
    }

    @Override
    public BaseResult<QuotaDto> getQuota(String quotaId){
        Quota quota = quotaManager.getQuotaById(quotaId);
        QuotaDto dto = new QuotaDto();
        BeanUtils.copyProperties(quota, dto);
        return new BaseResult(dto);
    }

    @Override
    public QuotaData getQuotaData(String quotaId, ConditionVo condition){
        Quota quota = quotaManager.getQuotaById(quotaId);
        condition.setQuotaCode(quota.getQuotaCode());
        List<GroupBy> groupBy = new ArrayList<>();
        String quotaDimStr = quota.getQuotaDim();// 指标维度，支持多维度，可以为空
        if(StringUtils.isNotEmpty(quotaDimStr)){
            String [] tmp = quotaDimStr.split(",");
            for(String s : tmp){
                groupBy.add(new GroupBy(s));
            }
        }
        condition.setGroupBy(groupBy);
        valid.valid(quota.getQuotaCode(), condition);
        Expression expression = new Expression(quota.getExpression(), "QUOTA");
        expression.init(condition);
        List<Map> mapList = expression.getResult();

        QuotaData result = new QuotaData();
        String oper = StringUtils.isEmpty(quota.getShowOperation())? "" : quota.getShowOperation();
        if(oper.startsWith("LIST")){
            if (CollectionUtils.isNotEmpty(mapList)) {
                List<Command> commands = CommandUtils.getCommands(oper, true);
                // 根据指标类型处理值
                if("NUM".equals(quota.getShowType())){
                    commands.add(new Command("R", "0"));
                } else if("MONEY".equals(quota.getShowType())){
                    commands.add(new Command("R", "2"));
                } else if("PERCENT".equals(quota.getShowType())){
                    commands.add(new Command("MULTIPLY", "100"));
                    commands.add(new Command("R", "2"));
                }
                Object value = mapList;
                if(CollectionUtils.isNotEmpty(commands)) {
                    for (Command cmd : commands) {
                        value = CommandUtils.executeCommand(cmd, value);
                    }
                }
                result.setData(value);
            }
        } else {
            // 根据指标类型处理值
            if (CollectionUtils.isNotEmpty(mapList) && mapList.get(0) != null) {
                List<Command> commands = CommandUtils.getCommands(oper, true);
                // 根据指标类型处理值
                if("NUM".equals(quota.getShowType())){
                    commands.add(new Command("R", "0"));
                } else if("MONEY".equals(quota.getShowType())){
                    commands.add(new Command("R", "2"));
                } else if("PERCENT".equals(quota.getShowType())){
                    commands.add(new Command("MULTIPLY", "100"));
                    commands.add(new Command("R", "2"));
                }
                Object value = mapList.get(0).get("value");
                if(CollectionUtils.isNotEmpty(commands)) {
                    for (Command cmd : commands) {
                        value = CommandUtils.executeCommand(cmd, value);
                    }
                }
                result.setData(value);
            }
        }
        return result;
    }

    @Override
    public QuotaData getChartData(ChartDto chartDto, ConditionVo condition) {
        ChartDto chartAga = chartManager.getChart(chartDto.getChartId());
        String startDateStr = condition.getStartCircleTime();
        String endDateStr = condition.getEndCircleTime();
        QuotaData result = new QuotaData();
        List<QuotaDto> quotaList = chartAga.getQuotaList();
        List cols = new ArrayList();
        boolean colsInit = false;
        List quotaDataInfo = new ArrayList();
        if(quotaList!=null) {
            if(ChartTypeEnum.TABLE.equals(chartAga.getType())){
                // table 比较特殊 需要特殊处理
                TableData tableData = null;
                // 遍历指标 生成列 code:name 获取数据
                List<String> colCodes = new ArrayList<>();
                // 获取图维度(表格格式时 各指标维度一致)
                ArrayList<GroupBy> chartGroupBy = new ArrayList<>();
                boolean initGb = false;
                for (QuotaDto quotaDto : quotaList) {
                    if(!initGb) {
                        String dimStr = quotaDto.getChartDimension();// 图表维度 必须有维度
                        if (StringUtils.isEmpty(dimStr)) {
                            dimStr = "CIRCLE_TIME";
                        }
                        String[] tmp = dimStr.split(",");
                        tableData = new TableData(tmp);
                        for (String s : tmp) {
                            cols.add(s + ":" + quotaManager.selectDimByDimCode(quotaDto.getQuotaId(), s));
                            colCodes.add(s);
                            chartGroupBy.add(new GroupBy(s));
                        }
                        condition.setGroupBy(chartGroupBy);
                        initGb = true;
                    }
                    // 设置列名
                    cols.add(quotaDto.getQuotaCode() + ":" + quotaDto.getQuotaDisplayName());
                    colCodes.add(quotaDto.getQuotaCode());
                }
                for (QuotaDto quotaDto : quotaList) {
                    condition.setQuotaCode(quotaDto.getQuotaCode());
                    // 条件校验
                    try {
                        valid.valid(quotaDto.getQuotaCode(), condition);
                    } catch (MetricException e) {
                        logger.warn("当前指标无此条件，查询结果为空", e);
                        if ("CHECK_CONDITION_01".equals(e.getCode())) {
                            return result;
                        }
                    }
                    // 获取数据
                    Expression expression = new Expression(quotaDto.getExpression(), "CHART");
                    expression.init(condition);
                    List<Map> mapList = expression.getResult();
                    // 运算
                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(mapList)) {
                        String oper = quotaDto.getShowOperation();
                        List<Command> commands = CommandUtils.getCommands(oper, false);
                        // 根据指标类型处理值
                        if("NUM".equals(quotaDto.getShowType())){
                            commands.add(new Command("R", "0"));
                        } else if("MONEY".equals(quotaDto.getShowType())){
                            commands.add(new Command("R", "2"));
                        } else if("PERCENT".equals(quotaDto.getShowType())){
                            commands.add(new Command("MULTIPLY", "100"));
                            commands.add(new Command("R", "2"));
                        }
                        if (CollectionUtils.isNotEmpty(commands)) {
                            for (Command cmd : commands) {
                                mapList = (List<Map>) CommandUtils.executeCommand(cmd, mapList);
                            }
                        }
                    }
                    for(Map tmp:mapList){
                        // 处理数据
                        TableDim dim = new TableDim();
                        /*Map vTmp = new HashMap();
                        StringBuilder quotaDimValue = new StringBuilder();*/
                        for(GroupBy gb : chartGroupBy) {
                            String gbValue = "";
                            if(gb.getCode().equals("CIRCLE_TIME")) {
                                gbValue = CircleTimeUtil.formatCircleTimeByParticleSize(((Timestamp)tmp.get(gb.getCode())), quotaDto.getParticleSize());
                            } else {
                                gbValue = tmp.get(gb.getCode())==null?"":tmp.get(gb.getCode()).toString();
                            }
                            dim.add(gbValue);
                           /* vTmp.put(gb.getCode(), gbValue);
                            quotaDimValue.append(gb.getCode()).append(":").append(gbValue).append("~");*/
                        }
                        tableData.addValue(dim, quotaDto.getQuotaCode(), tmp.get("value")==null?"0":tmp.get("value").toString());
                    }
                }
                result.setData(tableData.getValues());
            } else {
                for (QuotaDto quotaDto : quotaList) {
                    Date startDate = CircleTimeUtil.formatStrToDate(startDateStr, quotaDto.getParticleSize());
                    Date endDate = CircleTimeUtil.formatStrToDate(endDateStr, quotaDto.getParticleSize());
                    condition.setQuotaCode(quotaDto.getQuotaCode());
                    ArrayList<GroupBy> groupBy = new ArrayList<>();
                    ArrayList<GroupBy> chartGroupBy = new ArrayList<>();
                    // 获取图维度
                    String dimStr = quotaDto.getChartDimension();// 图表维度 必须有维度
                    if (StringUtils.isNotEmpty(dimStr)) {
                        String[] tmp = dimStr.split(",");
                        for (String s : tmp) {
                            chartGroupBy.add(new GroupBy(s));
                        }
                    }
                    if (ChartTypeEnum.PILE.equals(chartAga.getType()) ||
                            ChartTypeEnum.BROKENLINE.equals(chartAga.getType())) {
                        // 如果是堆积图 折线图 图维度不覆盖指标维度
                        // 获取指标维度
                        String quotaDimStr = quotaDto.getQuotaDim();// 指标维度，支持多维度，可以为空
                        if (StringUtils.isNotEmpty(quotaDimStr)) {
                            String[] tmp = quotaDimStr.split(",");
                            for (String s : tmp) {
                                groupBy.add(new GroupBy(s));
                            }
                        }
                        if (chartGroupBy.removeAll(groupBy)) {
                            if (CollectionUtils.isNotEmpty(chartGroupBy))
                                groupBy.addAll(chartGroupBy);
                        }
                    } else if (ChartTypeEnum.ROUND.equals(chartAga.getType())) {
                        // 如果是饼图 图维度覆盖指标维度
                        groupBy.addAll(chartGroupBy);
                    } else if (ChartTypeEnum.HISTOGRAMHORIZONTAL.equals(chartAga.getType())) {
                        // 如果是水平柱状 图维度覆盖指标维度
                        groupBy.addAll(chartGroupBy);
                    } else if (ChartTypeEnum.HISTOGRAMVERTICAL.equals(chartAga.getType())) {
                        // 如果是 垂直柱状 图维度覆盖指标维度
                        groupBy.addAll(chartGroupBy);
                    } else {
                        // 如果是其他 图维度不覆盖指标维度
                        // 获取指标维度
                        String quotaDimStr = quotaDto.getQuotaDim();// 指标维度，支持多维度，可以为空
                        if (StringUtils.isNotEmpty(quotaDimStr)) {
                            String[] tmp = quotaDimStr.split(",");
                            for (String s : tmp) {
                                groupBy.add(new GroupBy(s));
                            }
                        }
                        if (chartGroupBy.removeAll(groupBy)) {
                            if (CollectionUtils.isNotEmpty(chartGroupBy))
                                groupBy.addAll(chartGroupBy);
                        }
                    }
                    condition.setGroupBy(groupBy);
                    try {
                        valid.valid(quotaDto.getQuotaCode(), condition);
                    } catch (MetricException e) {
                        logger.warn("当前指标无此条件，查询结果为空", e);
                        if ("CHECK_CONDITION_01".equals(e.getCode())) {
                            return result;
                        }
                    }
                    Expression expression = new Expression(quotaDto.getExpression(), "CHART");
                    expression.init(condition);
                    List<Map> mapList = expression.getResult();

                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(mapList)) {
                        String oper = quotaDto.getShowOperation();
                        List<Command> commands = CommandUtils.getCommands(oper, false);
                        // 根据指标类型处理值
                        if("NUM".equals(quotaDto.getShowType())){
                            commands.add(new Command("R", "0"));
                        } else if("MONEY".equals(quotaDto.getShowType())){
                            commands.add(new Command("R", "2"));
                        } else if("PERCENT".equals(quotaDto.getShowType())){
                            commands.add(new Command("MULTIPLY", "100"));
                            commands.add(new Command("R", "2"));
                        }
                        if (CollectionUtils.isNotEmpty(commands)) {
                            for (Command cmd : commands) {
                                mapList = (List<Map>) CommandUtils.executeCommand(cmd, mapList);
                            }
                        }
                        if (ChartTypeEnum.PILE.equals(chartAga.getType()) ||
                                ChartTypeEnum.BROKENLINE.equals(chartAga.getType())) {
                            List data = new ArrayList();
                            // 如果是折线，先生成所有的日期列并初始化值
                            if (dimStr.equals("CIRCLE_TIME")) {
                                while (startDate.getTime() <= endDate.getTime()) {
                                    data.add("0");
                                    if (!colsInit) {
                                        cols.add(CircleTimeUtil.formatCircleTimeByParticleSize(startDate, quotaDto.getParticleSize()));
                                    }
                                    long nextTime = CircleTimeUtil.getNextCircleTime(startDate, quotaDto.getParticleSize());
                                    startDate.setTime(nextTime);
                                }
                                colsInit = true;
                            }
                            for (Map map : mapList) {
                                if (map == null) {
                                    continue;
                                }
                                String dimValue = map.get(dimStr) == null ? "" : map.get(dimStr).toString();
                                if (dimStr.equals("CIRCLE_TIME")) {
                                    String timeStr = CircleTimeUtil.formatCircleTimeByParticleSize(dimValue, quotaDto.getParticleSize());
                                    // 从开始时间到结束时间遍历 如果遇到相等插入值，如果遇到不相等设置值为0
                                    int i = 0;
                                    for (String col : (List<String>) cols) {
                                        if (timeStr.equals(col)) {
                                            Object value = map.get("value").toString();
                                            data.set(i, value);
                                            break;
                                        }
                                        i++;
                                    }
                                } else {
                                    cols.add(dimValue);
                                    Object value = map.get("value").toString();
                                    data.add(value);
                                }
                            }
                            Map quotaResult = new HashMap();
                            quotaResult.put("name", quotaDto.getQuotaName());
                            quotaResult.put("type", "line");
                            if (ChartTypeEnum.PILE.equals(chartAga.getType())) {
                                quotaResult.put("stack", "总量");// 写死
                                //  "areaStyle": {"normal": {}}, 写死
                                Map normal = new HashMap();
                                normal.put("normal", new HashMap<>());
                                quotaResult.put("areaStyle", normal);
                            }
                            quotaResult.put("data", data);
                            quotaDataInfo.add(quotaResult);
                        } else if (ChartTypeEnum.ROUND.equals(chartAga.getType())) {
                            for (Map map : mapList) {
                                if (map == null) {
                                    continue;
                                }
                                String dimValue = map.get(dimStr) == null ? "" : map.get(dimStr).toString();
                                if (dimStr.equals("CIRCLE_TIME")) {
                                    dimValue = CircleTimeUtil.formatCircleTimeByParticleSize(dimValue, quotaDto.getParticleSize());
                                }
                                cols.add(dimValue);
                                Map rowData = new HashMap();
                                Object value = map.get("value").toString();
                                rowData.put("value", value);
                                rowData.put("name", dimValue);
                                quotaDataInfo.add(rowData);
                            }
                        } else {
                            for (Map map : mapList) {
                                if (map == null) {
                                    continue;
                                }
                                String dimValue = map.get(dimStr) == null ? "" : map.get(dimStr).toString();
                                if (dimStr.equals("CIRCLE_TIME")) {
                                    cols.add(CircleTimeUtil.formatCircleTimeByParticleSize(dimValue, quotaDto.getParticleSize()));
                                } else {
                                    cols.add(dimValue);
                                }
                                Object value = map.get("value").toString();
                                quotaDataInfo.add(value);
                            }
                        }
                    }
                }
                result.setData(quotaDataInfo);
            }
            result.setCols(cols);
        }
        return result;
    }

    @Override
    public BaseResult<List<QuotaDto>> queryQuotaNameAndId(QuotaDto quotaDto) {
        return quotaManager.queryQuotaNameAndId(quotaDto);
    }

    @Override
    public List<QuotaDimensionDto> queryDimenListByChartId(String chartId){
        return quotaManager.queryDimenListByChartId(chartId);
    }

    @Override
    public List<QuotaDimensionDto> queryDimenListByBoardId(String boardId){
        return quotaManager.queryDimenListByBoardId(boardId);
    }

    @Override
    public List<QuotaDimensionDto> queryDimenListByQuotaId(String quotaId){
        return quotaManager.queryDimenListByQuotaId(quotaId);
    }

    @Override
    public List<Map> queryDimenListByQuotaCode(String[] quotaCodes){
        return quotaManager.queryDimenListByQuotaCode(quotaCodes);
    }

    @Override
    public List<String> getExecuteSql(String id, String type, ConditionVo condition){
        List<String> result = new ArrayList<>();
        if("QUOTA".equals(type)){
            Quota quota = quotaManager.getQuotaById(id);
            List<GroupBy> groupBy = new ArrayList<>();
            String quotaDimStr = quota.getQuotaDim();// 指标维度，支持多维度，可以为空
            if(StringUtils.isNotEmpty(quotaDimStr)){
                String [] tmp = quotaDimStr.split(",");
                for(String s : tmp){
                    groupBy.add(new GroupBy(s));
                }
            }
            condition.setGroupBy(groupBy);
            condition.setQuotaCode(quota.getQuotaCode());
            valid.valid(quota.getQuotaCode(), condition);
            Expression expression = new Expression(quota.getExpression(), type);
            result.add(expression.init(condition));
        } else if("CHART".equals(type)){
            ChartDto chart = chartManager.getChart(id);
            List<QuotaDto> quotaList = chart.getQuotaList();
            for(QuotaDto quotaDto:quotaList) {
                condition.setQuotaCode(quotaDto.getQuotaCode());
                List<GroupBy> groupBy = new ArrayList<>();
                String quotaDimStr = quotaDto.getQuotaDim();// 指标维度，支持多维度，可以为空
                if(StringUtils.isNotEmpty(quotaDimStr)){
                    String [] tmp = quotaDimStr.split(",");
                    for(String s : tmp){
                        groupBy.add(new GroupBy(s));
                    }
                }
                String chartDimStr = quotaDto.getChartDimension();// 图标维度 目前只支持单维度 且必须有维度
                GroupBy chartDim = new GroupBy(chartDimStr);
                if(!groupBy.contains(chartDim)) {
                    groupBy.add(chartDim);
                }
                condition.setGroupBy(groupBy);
                valid.valid(quotaDto.getQuotaCode(), condition);
                Expression expression = new Expression(quotaDto.getExpression(), type);
                result.add(expression.init(condition));
            }
        }
        return result;
    }

    @Override
    public PageData<QuotaDto> listQuotaPage(QuotaDto quotaDto) {
            try {
                PageData<QuotaDto> list = quotaManager.listQuotaPage(quotaDto);
                return list;
            } catch (Exception e){
                logger.error("分页获取指标：" + e.getMessage(), e);
                throw new MetricException("分页获取指标：" + e.getMessage(), e);
            }
    }

    @Override
    public BaseResult<QuotaDto> getQuotaByCode(String quotaCode) {
        Quota quota = quotaManager.getQuotaByCode(quotaCode);
        QuotaDto dto = new QuotaDto();
        if(quota !=null){
            BeanUtils.copyProperties(quota, dto);
        }
        return new BaseResult(dto);
    }
}
