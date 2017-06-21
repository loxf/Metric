package org.loxf.metric.biz.Operation;

import org.loxf.metric.base.exception.QuotaExpression;
import org.loxf.metric.biz.base.SpringApplicationContextUtil;
import org.loxf.metric.biz.utils.QuotaOperation;
import org.loxf.metric.biz.utils.QuotaSqlBuilder;
import org.loxf.metric.common.dto.ConditionVo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/5/9.
 */
public class Expression {
    private static Logger logger = LoggerFactory.getLogger(Expression.class);
    private String expression;
    private ConditionVo condition;
    private boolean isInit = false;
    private QuotaOperation quotaOperation;
    private String executeSql;//执行SQL
    private Map quotaSql = new HashMap();
    private String type ;

    /**
     * @param expression
     * @param type CHART / QUOTA
     */
    public Expression(String expression, String type){
        this.expression = expression;
        this.type = type;
        quotaOperation = (QuotaOperation)SpringApplicationContextUtil.getBean(QuotaOperation.class);
    }

    public String init(ConditionVo condition){
        this.condition = condition;
        logger.debug("初始化表达式：" + expression);
        // 获取表达式的指标
        List<String> quotaList = QuotaSqlBuilder.quotaList(expression);
        if(CollectionUtils.isNotEmpty(quotaList)){
            // 解析公式里面的指标
            if(CollectionUtils.isNotEmpty(quotaList)){
                for(String str : quotaList){
                    // 获取指标的SQL
                    String sql = quotaOperation.getSql(str, condition);
                    quotaSql.put(str, sql);
                }
            }
        }
        executeSql = quotaOperation.getSql(quotaSql, expression, type, condition);
        if(executeSql==null){
            logger.warn("指标表达式初始化失败。" + expression);
        } else {
            isInit = true;
        }
        return executeSql;
    }
    public List<Map> getResult() {
        if(!isInit){
            throw new QuotaExpression("公式未初始化");
        }
        logger.debug("执行sql：" + executeSql);
        return quotaOperation.query(executeSql);
    }

}
