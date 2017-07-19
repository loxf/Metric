package org.loxf.metric.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by luohj on 2017/5/15.
 */
@ApiModel("指标维度值")
public class QuotaDimensionValueDto extends BaseDto{
    @ApiModelProperty("指标维度值编码")
    private String dimValueCode;

    @ApiModelProperty("指标维度编码")
    private String dimCode;

    @ApiModelProperty("指标维度名称")
    private String dimName;

    @ApiModelProperty("指标维度值描述")
    private String dimValueDesc;

    @ApiModelProperty("指标维度值")
    private String dimValue;

    @ApiModelProperty("团队码")
    private String uniqueCode;

    @ApiModelProperty("账期")
    private Date circleTime;

    public String getDimValueCode() {
        return dimValueCode;
    }

    public void setDimValueCode(String dimValueCode) {
        this.dimValueCode = dimValueCode;
    }

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

    public String getDimValueDesc() {
        return dimValueDesc;
    }

    public void setDimValueDesc(String dimValueDesc) {
        this.dimValueDesc = dimValueDesc;
    }

    public String getDimValue() {
        return dimValue;
    }

    public void setDimValue(String dimValue) {
        this.dimValue = dimValue;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public Date getCircleTime() {
        return circleTime;
    }

    public void setCircleTime(Date circleTime) {
        this.circleTime = circleTime;
    }
}
