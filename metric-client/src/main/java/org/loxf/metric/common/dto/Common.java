package org.loxf.metric.common.dto;

import java.io.Serializable;

/**
 * Created by luohj on 2017/3/21.
 */
public class Common implements Serializable {
    private int start;
    private int end;
    private int row;
    private int currentPage;
    private String createdAtStart;
    private String createdAtEnd;
    private String updatedAtStart;
    private String updatedAtEnd;

    public int getStart() {
        if(this.getRow()>0 && this.getCurrentPage()>0){
            return (this.getCurrentPage()-1)*this.getRow();
        }
        return start;
    }

    public int getEnd()
    {
        if(this.getRow()>0 && this.getCurrentPage()>0){
            return this.getRow()*this.getCurrentPage();
        }
        return end;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getCreatedAtStart() {
        return createdAtStart;
    }

    public void setCreatedAtStart(String createdAtStart) {
        this.createdAtStart = createdAtStart;
    }

    public String getCreatedAtEnd() {
        return createdAtEnd;
    }

    public void setCreatedAtEnd(String createdAtEnd) {
        this.createdAtEnd = createdAtEnd;
    }

    public String getUpdatedAtStart() {
        return updatedAtStart;
    }

    public void setUpdatedAtStart(String updatedAtStart) {
        this.updatedAtStart = updatedAtStart;
    }

    public String getUpdatedAtEnd() {
        return updatedAtEnd;
    }

    public void setUpdatedAtEnd(String updatedAtEnd) {
        this.updatedAtEnd = updatedAtEnd;
    }
}
