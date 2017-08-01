package org.loxf.metric.base.ItemList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by hutingting on 2017/7/6.
 */
@ApiModel("指标项")
public class QuotaItem implements Serializable {

    @ApiModelProperty(value = "指标编码", required = true)
    private String quotaCode;

    @ApiModelProperty(value = "展示方式", required = true)
    private String showOperation;

    public String getQuotaCode() {
        return quotaCode;
    }

    public void setQuotaCode(String quotaCode) {
        this.quotaCode = quotaCode;
    }

    public String getShowOperation() {
        return showOperation;
    }

    public void setShowOperation(String showOperation) {
        this.showOperation = showOperation;
    }
}