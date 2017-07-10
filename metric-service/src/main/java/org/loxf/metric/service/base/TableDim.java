package org.loxf.metric.service.base;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luohj on 2017/6/19.
 */
public class TableDim {
    private List<String> valueList;

    public TableDim(){
        valueList = new ArrayList<>();
    }
    public String get(int index){
        return valueList.get(index);
    }

    public void add(String dimValue){
        valueList.add(dimValue);
    }
    @Override
    public boolean equals(Object object) {
        if (object instanceof TableDim) {
            return this.toString().equals(object.toString());
        }
        return false;
    }

    @Override
    public String toString(){
        StringBuilder valueStr = new StringBuilder();
        if(CollectionUtils.isNotEmpty(valueList)){
            for(String str : valueList) {
                valueStr.append(str).append("@");
            }
            return valueStr.toString();
        }
        return null;
    }

    @Override
    public int hashCode(){
        return this.toString().hashCode();
    }
}
