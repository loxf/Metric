package org.loxf.metric.dal.po;

import org.loxf.metric.base.ItemList.ChartItem;

import java.util.List;

public class Board extends BasePO{
    private String boardCode;

    private String boardName;

    private String state;

    private List<ChartItem> chartList;

    private String uniqueCode;

    public String getBoardCode() {
        return boardCode;
    }

    public void setBoardCode(String boardCode) {
        this.boardCode = boardCode;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<ChartItem> getChartList() {
        return chartList;
    }

    public void setChartList(List<ChartItem> chartList) {
        this.chartList = chartList;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

}