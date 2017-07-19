package org.loxf.metric.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by luohj on 2017/3/21.
 */
@ApiModel("分页")
public class Pager implements Serializable {
    /**
     * 开始
     */
    @ApiModelProperty("开始")
    private int start;
    /**
     * 结束
     */
    @ApiModelProperty("结束")
    private int end;
    /**
     * 每页数量
     */
    @ApiModelProperty("每页数量")
    private int rownum;
    /**
     * 当前页
     */
    @ApiModelProperty("当前页")
    private int currentPage;

    public int getStart() {
        if(this.getRownum()>0 && this.getCurrentPage()>0){
            return (this.getCurrentPage()-1)*this.getRownum();
        }
        return start;
    }

    public int getEnd()
    {
        if(this.getRownum()>0 && this.getCurrentPage()>0){
            return this.getRownum()*this.getCurrentPage();
        }
        return end;
    }

    public int getRownum() {
        return rownum;
    }

    public void setRownum(int rownum) {
        this.rownum = rownum;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
