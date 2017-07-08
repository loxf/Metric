package org.loxf.metric.common.dto;


import javafx.scene.chart.Chart;

import java.util.List;

public class BoardDto extends BaseDto{

    private String boardCode;

    private String boardName;

    private Integer state;

    private List<Chart> chartList;

    private String uniqueCode;

    private String createUserName;

    private  String updateUserName;

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public List<Chart> getChartList() {
        return chartList;
    }

    public void setChartList(List<Chart> chartList) {
        this.chartList = chartList;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

}