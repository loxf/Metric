package org.loxf.metric.dal.po;

import org.loxf.metric.base.ItermList.QuotaDimItem;

import java.util.Date;
import java.util.List;

public class Quota extends BasePO {
    private String quotaCode;

    private String quotaSource;

    private String expression;

    private String quotaName;

    private String type;

    private String showOperation;

    private String state;

    private List<QuotaDimItem> quotaDim;

    private String updateUserName;

    private String createUserName;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
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