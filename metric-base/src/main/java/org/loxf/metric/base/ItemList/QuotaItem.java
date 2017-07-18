package org.loxf.metric.base.ItemList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by hutingting on 2017/7/6.
 */
@ApiModel("指标项")
public class QuotaItem{

    @ApiModelProperty("指标编码")
    private String quotaCode;

    @ApiModelProperty("展示方式")
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