package org.loxf.metric.dal.po;

/**
 * Created by luohj on 2017/5/13.
 */
public class QuotaWithDim extends Quota {
    private String chartDimension;

    public String getChartDimension() {
        return chartDimension;
    }

    public void setChartDimension(String chartDimension) {
        this.chartDimension = chartDimension;
    }
}
