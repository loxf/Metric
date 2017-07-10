package org.loxf.metric.service.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/6/19.
 */
public class TableData {
    private String[] dimsTitleCode;// 维度CODE
    // 值域
    private List<Map> values = new ArrayList<>();

    private HashMap<TableDim, Integer> dimLocation = new HashMap<>();

    public TableData(String[] dimsTitleCode){
        this.dimsTitleCode = dimsTitleCode;
    }

    public void addValue(TableDim dim,String code, String value){
        if(dimLocation.containsKey(dim)){
            Integer loc = dimLocation.get(dim);
            Map tmp = values.get(loc.intValue());
            tmp.put(code, value);
        } else {
            Map tmp = new HashMap();
            // 设置DIM
            for(int i=0; i<dimsTitleCode.length; i++){
                tmp.put(dimsTitleCode[i], dim.get(i));
            }
            // 设置值
            tmp.put(code, value);
            values.add(tmp);
            dimLocation.put(dim, values.size()-1);
        }
    }

    public List<Map> getValues() {
        return values;
    }
}
