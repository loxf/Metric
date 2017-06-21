package org.loxf.metric.common.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by luohj on 2017/5/9.
 */
public class QuotaData implements Serializable {
    private Object data;
    private List<String> cols;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<String> getCols() {
        return cols;
    }

    public void setCols(List<String> cols) {
        this.cols = cols;
    }
}
