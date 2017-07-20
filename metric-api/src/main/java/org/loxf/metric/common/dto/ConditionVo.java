package org.loxf.metric.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "查询条件", description = "用于首页/看板条件过滤")
public class ConditionVo implements Serializable {
    @ApiModelProperty(value = "指标编码", hidden = true, readOnly = true)
    private String quotaCode;
    @ApiModelProperty(value = "图类型", hidden = true, readOnly = true)
    private String chartType;
    @ApiModelProperty(value = "开始时间", required = true)
    private String startCircleTime;
    @ApiModelProperty(value = "结束时间", required = true)
    private String endCircleTime;
    @ApiModelProperty("条件关系:AND/OR")
    private String relation;
    @ApiModelProperty("条件项")
    private List<Condition> condition;
    @ApiModelProperty(value = "默认条件", hidden = true, readOnly = true)
    private transient Map<String, ConditionVo> defaultCondition = new HashMap<>();
    @ApiModelProperty(value = "聚合列", hidden = true, readOnly = true)
    private List<GroupBy> groupBy;

    public ConditionVo(){}

    public String getQuotaCode() {
        return quotaCode;
    }

    public void setQuotaCode(String quotaCode) {
        this.quotaCode = quotaCode;
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
