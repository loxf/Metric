package org.loxf.metric.base.ItemList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by hutingting on 2017/7/6.
 */
@ApiModel("维度项")
public class QuotaDimItem implements Serializable {
    @ApiModelProperty(value = "维度编码", required = true)
    private String dimCode;

    @ApiModelProperty(value = "维度名称", required = true)
    private String dimName;

    public String getDimCode() {
        return dimCode;
    }

    public void setDimCode(String dimCode) {
        this.dimCode = dimCode;
    }

    public String getDimName() {
        return dimName;
    }

    public void setDimName(String dimName) {
        this.dimName = dimName;
    }
}
