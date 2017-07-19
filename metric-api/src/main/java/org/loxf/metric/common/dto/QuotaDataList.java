package org.loxf.metric.common.dto;

import io.swagger.annotations.ApiModel;

import java.util.*;

/**
 * 指标集合
 * Created by luohj on 2017/6/22.
 */
public class QuotaDataList {
    private List<QuotaData> list = new ArrayList<>();
    private transient HashMap<String, Integer> dataLocation = new HashMap();

    public void add(QuotaData quotaData){
        if(quotaData!=null) {
            list.add(quotaData);
            dataLocation.put(quotaData.getCompareStr(), list.size()-1);
        }
    }

    public QuotaData getByQuotaDim(String quotaDimsStr){
        if(quotaDimsStr==null){
            return null;
        }
        Integer loc = dataLocation.get(quotaDimsStr);
        if(loc==null){
            return null;
        }
        return list.get(loc);
    }

    public List<QuotaData> getList() {
        return list;
    }

    public Set<String> getAllDimStr(){
        Iterator keys = dataLocation.keySet().iterator();
        Set<String> set = new HashSet();
        while (keys.hasNext()){
            set.add((String) keys.next());
        }
        return set;
    }
}
