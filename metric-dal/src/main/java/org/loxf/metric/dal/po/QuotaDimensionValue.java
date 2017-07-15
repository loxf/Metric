package org.loxf.metric.dal.po;

import java.util.Date;

public class QuotaDimensionValue extends BasePO{
    private String dimValueCode;

    private String dimCode;

    private String dimName;

    private String dimValueDesc;

    private String dimValue;

    private String uniqueCode;

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
