package org.loxf.metric.dal.po;

/**
 * Created by luohj on 2017/3/21.
 */
public class Common {
    private int start;
    private int end;
    private int row;
    private String createdAtStart;
    private String createdAtEnd;
    private String updatedAtStart;
    private String updatedAtEnd;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
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
