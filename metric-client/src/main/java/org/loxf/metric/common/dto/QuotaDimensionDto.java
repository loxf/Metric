package org.loxf.metric.common.dto;

import java.util.Date;

/**
 * Created by luohj on 2017/5/4.
 */
public class QuotaDimensionDto extends BaseDto{

    private String dimCode;

    private String dimName;

    private String createdUser;

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

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}
