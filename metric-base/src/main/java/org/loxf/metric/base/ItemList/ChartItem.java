package org.loxf.metric.base.ItemList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by hutingting on 2017/7/6.
 */
@ApiModel("图项")
public class ChartItem implements Serializable{
    @ApiModelProperty(value = "图编码", required = true)
    private String chartCode;

    @ApiModelProperty(value = "图名称", required = true)
    private String chartName;

    @ApiModelProperty(value = "图的属性", required = true)
    private String properties;

    @ApiModelProperty(value = "顺序", hidden = true)
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
