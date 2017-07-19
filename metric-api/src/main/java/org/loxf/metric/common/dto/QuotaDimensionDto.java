package org.loxf.metric.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by luohj on 2017/5/4.
 */
@ApiModel("指标维度")
public class QuotaDimensionDto extends BaseDto{
    @ApiModelProperty("指标维度编码")
    private String dimCode;

    @ApiModelProperty("指标维度名称")
    private String dimName;

    @ApiModelProperty("团队码")
    private String uniqueCode;

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

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}
