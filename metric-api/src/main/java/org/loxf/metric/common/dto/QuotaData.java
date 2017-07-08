package org.loxf.metric.common.dto;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 指标数据
 * Created by luohj on 2017/6/22.
 */
public class QuotaData implements Serializable {
    /**
     * 指标维度
     */
    private List<QuotaDim> dims = new ArrayList<>();
    /**
     * 指标值
     */
    private BigDecimal value;

    private String compareStr;

    public boolean compareDim(QuotaData quotaData){
        if(quotaData.compareStr==null && this.compareStr==null){
            return true;
        } else if(quotaData.compareStr!=null && this.compareStr!=null && this.compareStr.equals(quotaData.compareStr) ){
            return true;
        } else {
            return false;
        }
    }

    public void putDim(String dimCode, String dimValue){
        dims.add(new QuotaDim(dimCode, dimValue));
        if(StringUtils.isEmpty(compareStr)){
            compareStr = dimCode + "$$" + dimValue;
        } else {
            compareStr += "-" + dimCode + "$$" + dimValue;
        }
    }

    public List<QuotaDim> getDims() {
        return dims;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getCompareStr() {
        return compareStr;
    }

    public String getDimValue(String dimCode){
        if(StringUtils.isEmpty(dimCode)){
            return "";
        }
        for(QuotaDim dim : dims){
            if(dimCode.equals(dim.getDimCode())){
                return dim.getDimValue();
            }
        }
        return "";
    }
}
