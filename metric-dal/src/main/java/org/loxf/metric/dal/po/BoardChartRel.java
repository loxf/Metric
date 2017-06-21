package org.loxf.metric.dal.po;

import java.util.Date;

public class BoardChartRel {
    private Long id;

    private String boardId;

    private String chartId;

    private int chartOrder;

    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId == null ? null : boardId.trim();
    }

    public String getChartId() {
        return chartId;
    }

    public void setChartId(String chartId) {
        this.chartId = chartId == null ? null : chartId.trim();
    }

    public int getChartOrder() {
        return chartOrder;
    }

    public void setChartOrder(int chartOrder) {
        this.chartOrder = chartOrder;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}