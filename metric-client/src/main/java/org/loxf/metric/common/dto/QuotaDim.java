package org.loxf.metric.common.dto;

import java.io.Serializable;

/**
 * 指标维度
 * Created by luohj on 2017/6/22.
 */
public class QuotaDim implements Serializable {
    /**
     * 指标维度CODE
     */
    private String dimCode ;
    /**
     * 指标维度值
     */
    private String dimValue ;

    public QuotaDim(){}

    public QuotaDim(String dimCode, String dimValue){
        this.dimCode = dimCode;
        this.dimValue = dimValue;
    }

    public String getDimCode() {
        return dimCode;
    }

    public void setDimCode(String dimCode) {
        this.dimCode = dimCode;
    }

    public String getDimValue() {
        return dimValue;
    }

    public void setDimValue(String dimValue) {
        this.dimValue = dimValue;
    }
}
