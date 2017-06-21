package org.loxf.metric.common.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by luohj on 2017/5/15.
 */
public class QuotaDimensionValueDto implements Serializable {
    private Long id;

    private String columnCode;

    private String columnValueName;

    private String columnValue;

    private Date createdAt;

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
        this.columnCode = columnCode;
    }

    public String getColumnValueName() {
        return columnValueName;
    }

    public void setColumnValueName(String columnValueName) {
        this.columnValueName = columnValueName;
    }

    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
