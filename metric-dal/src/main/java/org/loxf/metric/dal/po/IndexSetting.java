package org.loxf.metric.dal.po;

import org.loxf.metric.base.ItemList.ChartItem;

import java.util.List;

/**
 * Created by luohj on 2017/7/19.
 */
public class IndexSetting extends BasePO {
    private String indexCode;
    private String uniqueCode;
    private List<ChartItem> chartList;

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public List<ChartItem> getChartList() {
        return chartList;
    }

    public void setChartList(List<ChartItem> chartList) {
        this.chartList = chartList;
    }
}
