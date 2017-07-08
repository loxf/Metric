package org.loxf.metric.common.dto;

import java.util.List;

/**
 * Created by luohj on 2017/6/20.
 */
public class PageData<T> {
    /**
     * 数据
     */
    private List<T> rows;
    /**
     * 总记录数
     */
    private int totalRecords;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 当前页数
     */
    private int currentPage;
    /**
     * 每页行数
     */
    private int rownum;

    public PageData() {
    }

    public PageData(int records, List<T> rows) {
        this.totalRecords = records;
        this.rows = rows;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getTotalPage() {
        if(rownum==0){
            return 0;
        }
        return totalRecords % rownum == 0 ? totalRecords / rownum : totalRecords / rownum + 1;
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
