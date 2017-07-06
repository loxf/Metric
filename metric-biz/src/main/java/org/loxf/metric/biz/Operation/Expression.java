package org.loxf.metric.biz.Operation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.base.exception.QuotaExpression;
import org.loxf.metric.biz.base.SpringApplicationContextUtil;
import org.loxf.metric.biz.utils.Command;
import org.loxf.metric.biz.utils.CommandUtils;
import org.loxf.metric.biz.utils.QuotaOperation;
import org.loxf.metric.biz.utils.QuotaSqlBuilder;
import org.loxf.metric.common.dto.ConditionVo;
import org.loxf.metric.common.dto.QuotaData;
import org.loxf.metric.common.dto.QuotaDataList;
import org.loxf.metric.dal.po.Quota;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by luohj on 2017/5/9.
 */
public class Expression {
    private static Logger logger = LoggerFactory.getLogger(Expression.class);
    private QuotaOperation quotaOperation;

    /**
     * 指标运算表达式
     */
    private String expression;
    /**
     * 指标是否初始化
     */
    private boolean isInit = false;
    /**
     * 指标数据<String, List><指标CODE， 指标数据>
     */
    private HashMap<String, QuotaDataList> dataMap = new HashMap();
    /**
     * 是否是展示指标
     */
    private boolean forQuotaScan ;
    /**
     * 指定合计方式
     */
    private String summaryOperation ;

    /**
     * @param expression
     */
    public Expression(String expression, boolean forQuotaScan, String summaryOperation){
        this.expression = expression;
        this.forQuotaScan = forQuotaScan;
        this.summaryOperation = summaryOperation;
        quotaOperation = (QuotaOperation)SpringApplicationContextUtil.getBean(QuotaOperation.class);
    }

    public void init(ConditionVo condition){
        logger.debug("初始化表达式：" + expression);
        // 获取表达式的指标
        List<String> quotaList = QuotaSqlBuilder.quotaList(expression);
        if(CollectionUtils.isNotEmpty(quotaList)){
            // 获取指标的SQL
            Map<String, String> quotaSql = new HashMap();
            for(String str : quotaList){
                if(!quotaSql.containsKey(str)) {
                    String sql = quotaOperation.getSql(str, forQuotaScan,  condition);
                    logger.debug("构造sql：" + sql);
                    quotaSql.put(str, sql);
                }
            }
            // 获取指标数据
            for(String str: quotaList){
                if(!dataMap.containsKey(str)) {
                    String tmpSql = quotaSql.get(str);
                    logger.debug("执行sql：" + tmpSql);
                    QuotaDataList list = quotaOperation.query(tmpSql);
                    dataMap.put(str, list);
                }
            }
        }
        isInit = true;
    }
    public List<QuotaData> execute() {
        if(!isInit){
            throw new QuotaExpression("公式未初始化");
        }
        // 获取表达式的指标
        List<String> quotaList = QuotaSqlBuilder.quotaList(expression);
        // 单指标不需要运算
        if(quotaList.size()==0){
            return null;
        }
        // 获取标准运算式
        String execExpression = expression.replaceAll("\\$\\{([\\s\\S].*?)\\}", "%s");
        String testExpression = expression.replaceAll("\\$\\{([\\s\\S].*?)\\}", "1");
        // 返回结果
        List<QuotaData> quotaDataResult = new ArrayList<>();

        if(forQuotaScan){
            // 指标概览数据运算，先求出各指标的值
            List values = new ArrayList();
            for(String quotaCode : quotaList) {
                if(StringUtils.isEmpty(summaryOperation)) {
                    Quota q = quotaOperation.getQuota(quotaCode);
                    summaryOperation = q.getShowOperation();
                }
                if(summaryOperation.indexOf("COUNT")==-1) {// 非计数
                    List<Command> commands = CommandUtils.getCommands(summaryOperation, true);
                    QuotaDataList list = dataMap.get(quotaCode);
                    String value = "0";
                    if (list != null && CollectionUtils.isNotEmpty(list.getList())) {
                        Object o = CommandUtils.executeCommand(commands.get(0), list.getList());
                        if(o instanceof BigDecimal){
                            value = ((BigDecimal) o).toPlainString() + "";
                        } else {
                            value = o.toString();
                        }
                    }
                    values.add(value);
                } else{
                    QuotaDataList list = dataMap.get(quotaCode);
                    if (list != null && CollectionUtils.isNotEmpty(list.getList())) {
                        values.add(list.getList().get(0).getValue().toPlainString());
                    } else {
                        values.add("0");
                    }
                }
            }
            String exeStr = String.format(execExpression, values.toArray(new String[values.size()]));
            QuotaData quotaData = new QuotaData();
            if(quotaList.size()==1) {
                // 一个指标的时候 就直接是结果了。
                boolean isExpressionFlag = Arithmetic.isExpression(testExpression);
                if(isExpressionFlag){
                    String val = Arithmetic.arithmetic(exeStr);
                    quotaData.setValue(new BigDecimal(val));
                } else {
                    try {
                        BigDecimal b = new BigDecimal(exeStr);
                        quotaData.setValue(b);
                    } catch (NumberFormatException e) {
                        logger.warn("表达式直接转化数字错误：" + exeStr, e);
                    }
                }
            } else {
                String val = Arithmetic.arithmetic(exeStr);
                quotaData.setValue(new BigDecimal(val));
            }
            quotaDataResult.add(quotaData);
            return quotaDataResult;
        } else {
            // 获取基础数据的所有维度
            if (dataMap != null) {
                boolean isExpressionFlag = Arithmetic.isExpression(testExpression);
                if(!isExpressionFlag){
                    QuotaDataList quotaDataList = dataMap.get(quotaList.get(0));
                    return quotaDataList == null ? null : quotaDataList.getList();
                } else {
                    // 等待分发执行的运算式，目前不用，如果效率低，可以分发运算
                    HashMap<QuotaData, String> quotaDataExpressionMap = new HashMap<>();
                    Set<String> allDims = new HashSet<>();
                    Iterator ite = dataMap.keySet().iterator();
                    while (ite.hasNext()) {
                        String key = (String) ite.next();
                        QuotaDataList tmpData = dataMap.get(key);
                        allDims.addAll(tmpData.getAllDimStr());
                    }
                    // 根据维度去获取数据，并且组成可执行运算表达式
                    for (String dim : allDims) {
                        QuotaData dimQuotaData = null;
                        boolean initDim = false;
                        int i = 0;
                        String[] values = new String[quotaList.size()];
                        for (String quotaCode : quotaList) {
                            QuotaData tmp = dataMap.get(quotaCode).getByQuotaDim(dim);
                            if (tmp != null && !initDim) {
                                dimQuotaData = tmp;
                                initDim = true;
                            }
                            values[i++] = (tmp == null ? "0" : tmp.getValue().toPlainString() + "");
                        }
                        // 存放运算表达式
                        String exeStr = String.format(execExpression, values);
                        quotaDataExpressionMap.put(dimQuotaData, exeStr);
                        // 目前直接运算， 如果效率低， 可以走下面分发运算
                        String value = Arithmetic.arithmetic(exeStr);
                        dimQuotaData.setValue(new BigDecimal(value));
                        quotaDataResult.add(dimQuotaData);
                    }
                    // 多线程分发运算(未实现)
                    return quotaDataResult;
                }
            }
        }
        return null;
    }

}
