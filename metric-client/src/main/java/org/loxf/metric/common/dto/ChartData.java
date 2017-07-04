package org.loxf.metric.common.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/6/22.
 */
public class ChartData implements Serializable{
    private List<Map> data;
    private List<String> cols;

    public List<Map> getData() {
        return data;
    }

    public void setData(List<Map> data) {
        this.data = data;
    }

    public List<String> getCols() {
        return cols;
    }

    public void setCols(List<String> cols) {
        this.cols = cols;
    }
}
