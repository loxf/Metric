package org.loxf.metric.common.dto;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询条件
 * Created by luohj on 2017/5/9.
 */
public class ConditionVo implements Serializable {
    private String quotaCode;
    private String busiDomain;
    private String chartType;
    private String startCircleTime;
    private String endCircleTime;
    private String relation;
    private List<Condition> condition;
    private Map<String, ConditionVo> defaultCondition = new HashMap<>();
    private List<GroupBy> groupBy;

    public ConditionVo(){}

    public String getQuotaCode() {
        return quotaCode;
    }

    public void setQuotaCode(String quotaCode) {
        this.quotaCode = quotaCode;
    }

    public String getBusiDomain() {
        return busiDomain;
    }

    public void setBusiDomain(String busiDomain) {
        this.busiDomain = busiDomain;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getStartCircleTime() {
        return startCircleTime;
    }

    public void setStartCircleTime(String startCircleTime) {
        this.startCircleTime = startCircleTime;
    }

    public String getEndCircleTime() {
        return endCircleTime;
    }

    public void setEndCircleTime(String endCircleTime) {
        this.endCircleTime = endCircleTime;
    }

    public List<Condition> getCondition() {
        return condition;
    }

    public void setCondition(List<Condition> condition) {
        this.condition = condition;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public List<GroupBy> getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(List<GroupBy> groupBy) {
        this.groupBy = groupBy;
    }

    public Map<String, ConditionVo> getDefaultCondition() {
        return defaultCondition;
    }

    public void setDefaultCondition(Map<String, ConditionVo> defaultCondition) {
        this.defaultCondition = defaultCondition;
    }

    public String toPlainString(){
        StringBuilder sb = new StringBuilder();
        if(CollectionUtils.isNotEmpty(condition)){
            for(Condition cond : condition){
                sb.append(this.relation).append(" "+cond.getDesc()).append(" " + cond.getOper() + " ").
                        append(StringUtils.join(cond.getValue(), " ,")).append("\n");
            }
        }
        return sb.toString();
    }
}
