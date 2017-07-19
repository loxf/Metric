package org.loxf.metric.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.loxf.metric.base.ItemList.QuotaDimItem;

import java.util.List;

/**
 * Created by luohj on 2017/5/4.
 */
@ApiModel("指标")
public class QuotaDto extends BaseDto{
    @ApiModelProperty("指标编码")
    private String quotaCode;

    @ApiModelProperty("指标编码")
    private String quotaSource;

    @ApiModelProperty("指标名称")
    private String quotaName;

    @ApiModelProperty("指标类型:BASIC(基础指标)/COMPOSITE(复合指标)")
    private String type;

    @ApiModelProperty("聚合类型:SUM/AVG/MAX/MIN/COUNT")
    private String showOperation;

    @ApiModelProperty("展示类型:MONEY(金额)/NUMBER(数字)/PERCENT(百分比)/RATIO(比率)")
    private String showType;

    @ApiModelProperty("指标状态:AVAILABLE(生效)/DISABLED(失效)")
    private String state;

    @ApiModelProperty("数据接入方式:EXCEL/SDK")
    private String dataImportType;

    @ApiModelProperty("指标周期:1440(日)/-1(周)/-2(月)/-3(年)/>0数字(分钟)")
    private int intervalPeriod;

    @ApiModelProperty("指标维度")
    private List<QuotaDimItem> quotaDim;

    @ApiModelProperty("团队码")
    private String uniqueCode;

    public String getQuotaCode() {
        return quotaCode;
    }

    public void setQuotaCode(String quotaCode) {
        this.quotaCode = quotaCode;
    }

    public String getQuotaSource() {
        return quotaSource;
    }

    public void setQuotaSource(String quotaSource) {
        this.quotaSource = quotaSource;
    }

    public String getQuotaName() {
        return quotaName;
    }

    public void setQuotaName(String quotaName) {
        this.quotaName = quotaName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShowOperation() {
        return showOperation;
    }

    public void setShowOperation(String showOperation) {
        this.showOperation = showOperation;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getIntervalPeriod() {
        return intervalPeriod;
    }

    public void setIntervalPeriod(int intervalPeriod) {
        this.intervalPeriod = intervalPeriod;
    }

    public String getDataImportType() {
        return dataImportType;
    }

    public void setDataImportType(String dataImportType) {
        this.dataImportType = dataImportType;
    }

    public List<QuotaDimItem> getQuotaDim() {
        return quotaDim;
    }

    public void setQuotaDim(List<QuotaDimItem> quotaDim) {
        this.quotaDim = quotaDim;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

}
