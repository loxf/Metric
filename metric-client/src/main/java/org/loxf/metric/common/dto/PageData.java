package org.loxf.metric.common.dto;

import java.util.List;

/**
 * Created by luohj on 2017/6/20.
 */
public class PageData<T> {
    /** 数据 */
    private List<T> rows;
    /** 总记录数 */
    private int records;
    /** 总页数 */
    private int totalPage;
    /** 当前页数 */
    private int currentPage;
    /** 每页行数 */
    private int rownum;

    public PageData(){}

    public PageData(int totalPage, int records, List<T> rows){
        this.totalPage = totalPage;
        this.records = records;
        this.rows = rows;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getRownum() {
        return rownum;
    }

    public void setRownum(int rownum) {
        this.rownum = rownum;
    }
}
