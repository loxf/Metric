package org.loxf.metric.base.ItemList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by hutingting on 2017/7/6.
 */
@ApiModel("图项")
public class ChartItem{
    @ApiModelProperty("图编码")
    private String chartCode;

    @ApiModelProperty("图名称")
    private String chartName;

    @ApiModelProperty("图的属性")
    private String properties;

    @ApiModelProperty("顺序")
    private int order;

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

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
