package org.loxf.metric.common.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.loxf.metric.base.ItemList.ChartItem;

import java.util.List;
@ApiModel("看板")
public class BoardDto extends BaseDto{
    @ApiModelProperty("看板编码")
    private String boardCode;

    @ApiModelProperty("看板名称")
    private String boardName;

    @ApiModelProperty(value = "看板状态:AVAILABLE(生效)/DISABLED(失效)", hidden = true)
    private String state;

    @ApiModelProperty("图项列表")
    private List<ChartItem> chartList;

    @ApiModelProperty("团队码")
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