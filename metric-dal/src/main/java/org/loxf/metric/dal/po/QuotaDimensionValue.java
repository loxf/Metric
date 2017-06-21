package org.loxf.metric.dal.po;

import java.util.Date;

public class QuotaDimensionValue {
    private Long id;

    private String columnCode;

    private String columnValueName;

    private String columnValue;

    private Date createdAt;

    public QuotaDimensionValue(){}

    public QuotaDimensionValue(String columnCode, String columnValue, String columnValueName){
        this.columnCode = columnCode;
        this.columnValue = columnValue;
        this.columnValueName = columnValueName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode == null ? null : columnCode.trim();
    }

    public String getColumnValueName() {
        return columnValueName;
    }

    public void setColumnValueName(String columnValueName) {
        this.columnValueName = columnValueName == null ? null : columnValueName.trim();
    }

    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue == null ? null : columnValue.trim();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}