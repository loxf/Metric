package org.loxf.metric.service.Operation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.base.exception.QuotaExpression;
import org.loxf.metric.common.constants.QuotaType;
import org.loxf.metric.service.base.SpringApplicationContextUtil;
import org.loxf.metric.service.utils.*;
import org.loxf.metric.common.dto.ConditionVo;
import org.loxf.metric.common.dto.QuotaData;
import org.loxf.metric.common.dto.QuotaDataList;
import org.loxf.metric.dal.po.Quota;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

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

    public void init(ConditionVo condition, String defaultCondition){
        logger.debug("初始化表达式：" + expression);
        // 复制一个ConditionVo
        ConditionVo vo = new ConditionVo();
        BeanUtils.copyProperties(condition, vo);
        // 获取指标与条件的映射关系
        Map<String, ConditionVo> quotaConditionMap = new HashMap<>();
        List<String> conditionList = new ArrayList<>();
        if(StringUtils.isNotEmpty(defaultCondition)){
            conditionList.add(defaultCondition);
        }
        this.expression = explainExpression(expression, vo, conditionList, quotaConditionMap);
        logger.debug("执行公式：" + expression);
        // 获取指标数据
        Iterator<String> keyIte = quotaConditionMap.keySet().iterator();
        while (keyIte.hasNext()){
            String key = keyIte.next();
            if(!dataMap.containsKey(key)) {
                String sql = quotaOperation.getSql(key.split("-")[0], forQuotaScan,  quotaConditionMap.get(key));
                logger.debug("执行sql：" + sql);
                // 获取指标数据
                QuotaDataList list = quotaOperation.query(sql);
                dataMap.put(key, list);
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
            for(String quotaCodeStr : quotaList) {
                String quotaCode = quotaCodeStr.split("-")[0];
                if(StringUtils.isEmpty(summaryOperation)) {
                    Quota q = quotaOperation.getQuota(quotaCode);
                    summaryOperation = q.getShowOperation();
                }
                if(summaryOperation.indexOf("COUNT")==-1) {// 非计数
                    List<Command> commands = CommandUtils.getCommands(summaryOperation, true);
                    QuotaDataList list = dataMap.get(quotaCodeStr);
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
                    QuotaDataList list = dataMap.get(quotaCodeStr);
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
                        for (String quotaCodeStr : quotaList) {
                            QuotaData tmp = dataMap.get(quotaCodeStr).getByQuotaDim(dim);
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

    /**
     * 解析公式（迭代）
     * @param expression 公式
     * @param requestCondition 用户请求条件
     * @param conditionList 默认条件列表
     * @param result 结果
     * @return
     */
    private String explainExpression(String expression, ConditionVo requestCondition, List<String> conditionList, Map result){
        // 获取源公式包含的指标列表
        String execExpression = expression;
        List<String> quotaList = QuotaSqlBuilder.quotaList(expression);
        if(CollectionUtils.isNotEmpty(quotaList)) {

            // 处理公式中的指标
            for (String str : quotaList) {
                // 获取指标信息
                Quota quota = quotaOperation.getQuota(str);
                if(quota.getType().equals(QuotaType.COMPOSITE.getValue())){
                    // 复合指标的默认条件处理
                    if(StringUtils.isNotEmpty(quota.getDefaultCondition())){
                        conditionList.add(quota.getDefaultCondition());
                    }
                    execExpression = execExpression.replace(QuotaSqlBuilder.getQuotaExpress(str),
                            explainExpression(quota.getQuotaSource(), requestCondition, conditionList , result));
                } else {
                    // 基础指标是退出的条件
                    // 解析默认条件列表
                    List<ConditionVo> conditionVoList = new ArrayList<>();
                    if(CollectionUtils.isNotEmpty(conditionList)){
                        for(String conditionStr : conditionList) {
                            ConditionVo def = new ConditionVo();
                            ConditionUtil.parseCondition(conditionStr, def);
                            conditionVoList.add(def);
                        }
                    }
                    ConditionVo useCondition = new ConditionVo();
                    BeanUtils.copyProperties(requestCondition, useCondition);
                    if(CollectionUtils.isNotEmpty(conditionVoList)){
                        int count = 0;
                        // 将默认条件添加到请求条件
                        Map mapCondition = new HashMap();
                        for(ConditionVo def : conditionVoList) {
                            mapCondition.put("QUOTA" + count++, def);
                        }
                        useCondition.setDefaultCondition(mapCondition);
                    }
                    // 将指标与请求条件关联。因为同一个基础指标在公式中可能引用很多次，根据默认条件的hashcode进行判断是否是同样的默认条件
                    String key = str + "-" + Math.abs(conditionList.hashCode());
                    result.put(key, useCondition);
                    execExpression = execExpression.replace(QuotaSqlBuilder.getQuotaExpress(str), QuotaSqlBuilder.getQuotaExpress(key));
                }
            }
        }
        return execExpression;
    }
}
