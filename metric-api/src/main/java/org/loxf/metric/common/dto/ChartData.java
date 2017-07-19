package org.loxf.metric.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/6/22.
 */
@ApiModel("图数据")
public class ChartData implements Serializable{
    @ApiModelProperty("图数据")
    private List<Map> data;
    @ApiModelProperty("图列表")
    private List<String> cols;

    public List<Map> getData() {
        return data;
    }

    public void setData(List<Map> data) {
        this.data = data;
    }

    public List<String> getCols() {
        return cols;
    }

    public void setCols(List<String> cols) {
        this.cols = cols;
    }
}
