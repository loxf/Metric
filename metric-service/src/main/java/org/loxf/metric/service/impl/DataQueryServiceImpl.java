package org.loxf.metric.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.api.IDataQueryService;
import org.loxf.metric.base.ItemList.ChartItem;
import org.loxf.metric.base.ItemList.QuotaItem;
import org.loxf.metric.base.constants.DefaultDimCode;
import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.common.constants.ChartTypeEnum;
import org.loxf.metric.common.constants.QuotaType;
import org.loxf.metric.common.dto.*;
import org.loxf.metric.dal.dao.interfaces.*;
import org.loxf.metric.dal.po.*;
import org.loxf.metric.service.Operation.Expression;
import org.loxf.metric.service.base.TableData;
import org.loxf.metric.service.base.TableDim;
import org.loxf.metric.service.callable.QueryChartDataCallable;
import org.loxf.metric.service.session.UserSessionManager;
import org.loxf.metric.service.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by luohj on 2017/7/15.
 */
@Service
public class DataQueryServiceImpl implements IDataQueryService {
    private static Logger logger = LoggerFactory.getLogger(DataQueryServiceImpl.class);
    @Autowired
    private QuotaDao quotaDao;
    @Autowired
    private BoardDao boardDao;
    @Autowired
    private DataDao dataDao;
    @Autowired
    private ChartDao chartDao;
    @Autowired
    private QuotaDimensionDao quotaDimensionDao;
    @Autowired
    private QuotaDimensionValueDao quotaDimensionValueDao;
    @Autowired
    private ValidConditionUtil valid;

    @Override
    public List<ChartData> getBoardData(String handleUserName, String boardCode, ConditionVo condition) {
        Board param = new Board();
        param.setBoardCode(boardCode);
        Board board = boardDao.findOne(param);
        if(CollectionUtils.isNotEmpty(board.getChartList())){
            List<ChartData> chartDataList = new ArrayList<>();
            List<ChartItem> chartList = board.getChartList();
            List<Future<ChartData>> futureList = new ArrayList<>();
            for(ChartItem chartItem : chartList){
                // Future
                QueryChartDataCallable callable = new QueryChartDataCallable(handleUserName,
                        chartItem.getChartCode(), condition, this);
                Future<ChartData> future = PoolUtil.getPool().submit(callable);
                futureList.add(future);
            }
            for(Future future : futureList){
                try {
                    ChartData chartData = (ChartData)future.get();
                    if(chartData!=null){
                        chartDataList.add(chartData);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            return chartDataList;
        }
        return null;
    }
    @Override
    public ChartData getChartData(String handleUserName, String chartCode, ConditionVo condition) {
        User user = UserSessionManager.getSession().getUserSession(handleUserName);
        return getChartData(user, chartCode, condition);
    }

    private ChartData getChartData(User user, String chartCode, ConditionVo condition) {
        ChartData result = new ChartData();
        Chart chart = chartDao.findByCode(chartCode, user.getUserName());
        // 设置默认的图条件
        if (StringUtils.isNotEmpty(chart.getDefaultCondition())) {
            try {
                ConditionVo def = new ConditionVo();
                ConditionUtil.parseCondition(URLDecoder.decode(chart.getDefaultCondition(), "utf-8"), def);
                condition.getDefaultCondition().put("CHART", def);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String startDateStr = condition.getStartCircleTime();
        String endDateStr = condition.getEndCircleTime();
        List<QuotaItem> quotaList = chart.getQuotaList();
        List cols = new ArrayList();
        boolean colsInit = false;
        List quotaDataInfo = new ArrayList();
        if (quotaList != null) {
            if (ChartTypeEnum.TABLE.equals(chart.getType())) {
                // table 比较特殊 需要特殊处理
                TableData tableData = null;
                // 遍历指标 生成列 code:name 获取数据
                List<String> colCodes = new ArrayList<>();
                // 获取图维度(表格格式时 各指标维度一致)
                ArrayList<GroupBy> chartGroupBy = new ArrayList<>();
                // 设置维度信息
                String dimStr = chart.getChartDimension();// 图表维度 必须有维度
                if (StringUtils.isEmpty(dimStr)) {
                    dimStr = DefaultDimCode.CIRCLE_TIME;
                }
                String[] tmpArr = dimStr.split(",");
                tableData = new TableData(tmpArr);
                for (String s : tmpArr) {
                    QuotaDimension qd = new QuotaDimension();
                    qd.setDimCode(s);
                    String str = s + ":" + quotaDimensionDao.findOne(qd).getDimName();
                    if (DefaultDimCode.CIRCLE_TIME.equals(s)) {
                        str = str + ":DATE";
                    } else {
                        str = str + ":TEXT";
                    }
                    cols.add(str);
                    colCodes.add(s);
                    chartGroupBy.add(new GroupBy(s));
                }
                condition.setGroupBy(chartGroupBy);

                for (QuotaItem quotaItem : quotaList) {
                    // 获取指标
                    Quota param = new Quota();
                    param.setQuotaCode(quotaItem.getQuotaCode());
                    Quota quota = quotaDao.findOne(param);
                    // 设置条件指标
                    condition.setQuotaCode(quotaItem.getQuotaCode());
                    // 条件校验
                    try {
                        valid.valid(quotaItem.getQuotaCode(), condition);
                    } catch (MetricException e) {
                        logger.warn("当前指标无此条件，查询结果为空", e);
                        if ("CHECK_CONDITION_01".equals(e.getCode())) {
                            return result;
                        }
                    }
                    // 获取数据
                    Expression expression = new Expression(quota.getType().equals(QuotaType.BASIC.getValue()) ?
                            QuotaSqlBuilder.getQuotaExpress(quota.getQuotaCode()) : quota.getQuotaSource(),
                            false, null);
                    try {
                        expression.init(condition, URLDecoder.decode(quota.getDefaultCondition(), "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        logger.error("解码默认条件错误：", e);
                    }
                    List<QuotaData> mapList = expression.execute();
                    // 运算
                    if (CollectionUtils.isNotEmpty(mapList)) {
                        String oper = quota.getShowOperation();
                        List<Command> commands = CommandUtils.getCommands(oper, false);
                        // 根据指标类型处理值
                        if ("NUM".equals(quota.getShowType())) {
                            commands.add(new Command("R", "0"));
                        } else if ("MONEY".equals(quota.getShowType())) {
                            commands.add(new Command("R", "2"));
                        } else if ("PERCENT".equals(quota.getShowType())) {
                            commands.add(new Command("MULTIPLY", "100"));
                            commands.add(new Command("R", "2"));
                        }
                        if (CollectionUtils.isNotEmpty(commands)) {
                            for (Command cmd : commands) {
                                mapList = (List<QuotaData>) CommandUtils.executeCommand(cmd, mapList);
                            }
                        }
                    }
                    for (QuotaData tmp : mapList) {
                        // 处理数据
                        TableDim dim = new TableDim();
                        for (GroupBy gb : chartGroupBy) {
                            String gbValue = "";
                            if (gb.getCode().equals("CIRCLE_TIME")) {
                                gbValue = CircleTimeUtil.formatCircleTimeByParticleSize((tmp.getDimValue(gb.getCode())), quota.getIntervalPeriod());
                            } else {
                                gbValue = tmp.getDimValue(gb.getCode());
                            }
                            dim.add(gbValue);
                        }
                        tableData.addValue(dim, quota.getQuotaCode(), tmp.getValue() == null ? "0" : tmp.getValue().toPlainString());
                    }

                    // 设置列名 计算汇总
                    // 获取图上的计算方式
                    String showOperation = quotaItem.getShowOperation();
                    if (StringUtils.isNotEmpty(showOperation)) {
                        showOperation = quota.getShowOperation();
                    }
                    String summaryOper = showOperation.split(" ")[0];
                    String summaryStr = getNameByOper(summaryOper);
                    if (CollectionUtils.isNotEmpty(mapList)) {
                        Command command = new Command(summaryOper.equals("COUNT") ? "SUM" : summaryOper, "value");
                        Object summaryValue = CommandUtils.executeCommand(command, mapList);
                        if (summaryValue instanceof BigDecimal) {
                            summaryValue = ((BigDecimal) summaryValue).toPlainString();
                        } else {
                            summaryValue = summaryValue.toString();
                        }
                        summaryStr = summaryStr + "[" + summaryValue + "]";
                    } else {
                        summaryStr = summaryStr + "[ 0 ]";
                    }
                    cols.add(quota.getQuotaCode() + ":" + quota.getQuotaName() +
                            ":" + ("NUM".equalsIgnoreCase(quota.getShowType()) ? "INT" : "FLOAT") + ":" + summaryStr);
                    colCodes.add(quota.getQuotaCode());
                }
                result.setData(tableData.getValues());
            } else {
                for (QuotaItem quotaItem : quotaList) {
                    // 获取指标
                    Quota param = new Quota();
                    param.setQuotaCode(quotaItem.getQuotaCode());
                    Quota quota = quotaDao.findOne(param);

                    Date startDate = CircleTimeUtil.formatStrToDate(startDateStr, quota.getIntervalPeriod());
                    Date endDate = CircleTimeUtil.formatStrToDate(endDateStr, quota.getIntervalPeriod());
                    condition.setQuotaCode(quota.getQuotaCode());
                    ArrayList<GroupBy> chartGroupBy = new ArrayList<>();
                    // 获取图维度
                    String dimStr = chart.getChartDimension();// 图表维度 必须有维度
                    if (StringUtils.isNotEmpty(dimStr)) {
                        String[] tmp = dimStr.split(",");
                        for (String s : tmp) {
                            chartGroupBy.add(new GroupBy(s));
                        }
                    }
                    condition.setGroupBy(chartGroupBy);
                    try {
                        valid.valid(quota.getQuotaCode(), condition);
                    } catch (MetricException e) {
                        logger.warn("当前指标无此条件，查询结果为空", e);
                        if ("CHECK_CONDITION_01".equals(e.getCode())) {
                            return result;
                        }
                    }
                    Expression expression = new Expression(quota.getType().equals(QuotaType.BASIC.getValue()) ?
                            QuotaSqlBuilder.getQuotaExpress(quota.getQuotaCode()) : quota.getQuotaSource(),
                            false, null);
                    try {
                        expression.init(condition, URLDecoder.decode(quota.getDefaultCondition(), "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        logger.error("解码默认条件错误：", e);
                    }
                    List<QuotaData> mapList = expression.execute();
                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(mapList)) {
                        String oper = quota.getShowOperation();
                        List<Command> commands = CommandUtils.getCommands(oper, false);
                        // 根据指标类型处理值
                        if ("NUM".equals(quota.getShowType())) {
                            commands.add(new Command("R", "0"));
                        } else if ("MONEY".equals(quota.getShowType())) {
                            commands.add(new Command("R", "2"));
                        } else if ("PERCENT".equals(quota.getShowType())) {
                            commands.add(new Command("MULTIPLY", "100"));
                            commands.add(new Command("R", "2"));
                        }
                        if (CollectionUtils.isNotEmpty(commands)) {
                            for (Command cmd : commands) {
                                mapList = (List<QuotaData>) CommandUtils.executeCommand(cmd, mapList);
                            }
                        }
                        if (ChartTypeEnum.PILE.equals(chart.getType()) ||
                                ChartTypeEnum.BROKENLINE.equals(chart.getType())) {
                            List data = new ArrayList();
                            // 如果是折线，先生成所有的日期列并初始化值
                            if (dimStr.equals("CIRCLE_TIME")) {
                                while (startDate.getTime() <= endDate.getTime()) {
                                    data.add("0");
                                    if (!colsInit) {
                                        cols.add(CircleTimeUtil.formatCircleTimeByParticleSize(startDate, quota.getIntervalPeriod()));
                                    }
                                    long nextTime = CircleTimeUtil.getNextCircleTime(startDate, quota.getIntervalPeriod());
                                    startDate.setTime(nextTime);
                                }
                                colsInit = true;
                            }
                            for (QuotaData map : mapList) {
                                if (map == null) {
                                    continue;
                                }
                                String dimValue = map.getDimValue(dimStr);
                                if (dimStr.equals("CIRCLE_TIME")) {
                                    String timeStr = CircleTimeUtil.formatCircleTimeByParticleSize(dimValue, quota.getIntervalPeriod());
                                    // 从开始时间到结束时间遍历 如果遇到相等插入值，如果遇到不相等设置值为0
                                    int i = 0;
                                    for (String col : (List<String>) cols) {
                                        if (timeStr.equals(col)) {
                                            Object value = map.getValue().toPlainString();
                                            data.set(i, value);
                                            break;
                                        }
                                        i++;
                                    }
                                } else {
                                    cols.add(dimValue);
                                    Object value = map.getValue().toPlainString();
                                    data.add(value);
                                }
                            }
                            Map quotaResult = new HashMap();
                            quotaResult.put("name", quota.getQuotaName());
                            quotaResult.put("type", "line");
                            if (ChartTypeEnum.PILE.equals(chart.getType())) {
                                quotaResult.put("stack", "总量");// 写死
                                //  "areaStyle": {"normal": {}}, 写死
                                Map normal = new HashMap();
                                normal.put("normal", new HashMap<>());
                                quotaResult.put("areaStyle", normal);
                            }
                            quotaResult.put("data", data);
                            quotaDataInfo.add(quotaResult);
                        } else if (ChartTypeEnum.ROUND.equals(chart.getType())) {
                            for (QuotaData map : mapList) {
                                if (map == null) {
                                    continue;
                                }
                                String dimValue = map.getDimValue(dimStr);
                                if (dimStr.equals("CIRCLE_TIME")) {
                                    dimValue = CircleTimeUtil.formatCircleTimeByParticleSize(dimValue, quota.getIntervalPeriod());
                                }
                                cols.add(dimValue);
                                Map rowData = new HashMap();
                                Object value = map.getValue().toPlainString();
                                rowData.put("value", value);
                                rowData.put("name", dimValue);
                                quotaDataInfo.add(rowData);
                            }
                        } else {
                            for (QuotaData map : mapList) {
                                if (map == null) {
                                    continue;
                                }
                                String dimValue = map.getDimValue(dimStr);
                                if (dimStr.equals("CIRCLE_TIME")) {
                                    cols.add(CircleTimeUtil.formatCircleTimeByParticleSize(dimValue, quota.getIntervalPeriod()));
                                } else {
                                    cols.add(dimValue);
                                }
                                Object value = map.getValue().toPlainString();
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

    private String getNameByOper(String oper) {
        if ("SUM".equalsIgnoreCase(oper)) {
            return "合计";
        } else if ("AVG".equalsIgnoreCase(oper)) {
            return "平均";
        } else if ("MAX".equalsIgnoreCase(oper)) {
            return "最大";
        } else if ("MIN".equalsIgnoreCase(oper)) {
            return "最小";
        } else if ("COUNT".equalsIgnoreCase(oper)) {
            return "数量";
        } else {
            return oper;
        }
    }

}
