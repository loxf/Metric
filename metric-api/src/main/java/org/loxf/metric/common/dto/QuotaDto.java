package org.loxf.metric.common.dto;

import java.util.List;

/**
 * Created by luohj on 2017/5/4.
 */
public class QuotaDto extends BaseDto{

    private String quotaCode;

    private String quotaSource;

    private String expression;

    private String quotaName;

    private String type;

    private String showOperation;

    private Integer state;

    private List<QuotaDimensionDto> quotaDim;

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

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public List<QuotaDimensionDto> getQuotaDim() {
        return quotaDim;
    }

    public void setQuotaDim(List<QuotaDimensionDto> quotaDim) {
        this.quotaDim = quotaDim;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

}
