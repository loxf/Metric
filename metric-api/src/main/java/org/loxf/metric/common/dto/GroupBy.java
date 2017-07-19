package org.loxf.metric.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by luohj on 2017/5/10.
 */
@ApiModel("分组")
public class GroupBy implements Serializable {
    public GroupBy(String code){
        this.code = code;
    }
    @ApiModelProperty("分组列编码，即指标维度编码")
    private String code ;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString(){
        return code;
    }
    @Override
    public int hashCode(){
        return super.hashCode();
    }
    @Override
    public boolean equals(Object object){
        if(object==null){
            return false;
        }
        if(code.equals(object.toString())){
            return true;
        }
        return false;
    }
}
