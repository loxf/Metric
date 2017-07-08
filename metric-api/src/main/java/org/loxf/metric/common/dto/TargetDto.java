package org.loxf.metric.common.dto;

import java.util.Date;
import java.util.List;

public class TargetDto extends BaseDto{

    private String targetCode;

    private String targetName;

    private String targetDesc;

    private Integer state;

    private Date targetStartTime;

    private Date targetEndTime;

    private String uniqueCode;

    private String createUserName;

    private String updatedUserName;

    private List<TargetItem> itemList;

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName == null ? null : targetName.trim();
    }

    public String getTargetDesc() {
        return targetDesc;
    }

    public void setTargetDesc(String targetDesc) {
        this.targetDesc = targetDesc == null ? null : targetDesc.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getTargetStartTime() {
        return targetStartTime;
    }

    public void setTargetStartTime(Date targetStartTime) {
        this.targetStartTime = targetStartTime;
    }

    public Date getTargetEndTime() {
        return targetEndTime;
    }

    public void setTargetEndTime(Date targetEndTime) {
        this.targetEndTime = targetEndTime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getUpdatedUserName() {
        return updatedUserName;
    }

    public void setUpdatedUserName(String updatedUserName) {
        this.updatedUserName = updatedUserName;
    }

    public List<TargetItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<TargetItem> itemList) {
        this.itemList = itemList;
    }
}