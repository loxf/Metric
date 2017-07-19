package org.loxf.metric.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.loxf.metric.base.ItemList.QuotaItem;
import org.loxf.metric.base.ItemList.VisibleItem;

import java.util.List;
import java.util.Set;

@ApiModel("图")
public class ChartDto extends BaseDto {

    @ApiModelProperty(value = "图编码", required = true, example = "KSDU92WEJGYU34IFI9", position = 1)
    private String chartCode;

    @ApiModelProperty(value = "图名称", example = "收入折线图", position = 2)
    private String chartName;

    @ApiModelProperty(value = "指标列表", position = 3)
    private List<QuotaItem> quotaList;

    @ApiModelProperty(value = "图类型", example = "brokenLine", position = 4)
    private String type;

    @ApiModelProperty(value = "默认查询条件", position = 5)
    private String defaultCondition;

    @ApiModelProperty(value = "图维度", example = "circleTime", position = 6)
    private String chartDimension;

    @ApiModelProperty(value = "可见范围:SPECIFICRANGE/ALL", example = "SPECIFICRANGE", position = 7)
    private String visibleType;

    @ApiModelProperty(value = "可见列表", position = 8)
    private List<VisibleItem> visibleList;

    @ApiModelProperty(value = "状态:AVAILABLE(生效)/DISABLED(失效)", example = "AVAILABLE", position = 10)
    private String state;

    @ApiModelProperty(value = "团队码", example = "KX45Khdfk95k", position = 11)
    private String uniqueCode;

    public String getChartCode() {
        return chartCode;
    }

    public void setChartCode(String chartCode) {
        this.chartCode = chartCode;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getVisibleType() {
        return visibleType;
    }

    public void setVisibleType(String visibleType) {
        this.visibleType = visibleType;
    }

    public String getDefaultCondition() {
        return defaultCondition;
    }

    public void setDefaultCondition(String defaultCondition) {
        this.defaultCondition = defaultCondition;
    }

    public String getChartDimension() {
        return chartDimension;
    }

    public void setChartDimension(String chartDimension) {
        this.chartDimension = chartDimension;
    }

    public List<QuotaItem> getQuotaList() {
        return quotaList;
    }

    public void setQuotaList(List<QuotaItem> quotaList) {
        this.quotaList = quotaList;
    }

    public List<VisibleItem> getVisibleList() {
        return visibleList;
    }

    public void setVisibleList(List<VisibleItem> visibleList) {
        this.visibleList = visibleList;
    }
}