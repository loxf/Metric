package org.loxf.metric.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hutingting on 2017/7/6.
 */
@ApiModel("基础数据")
public class BaseDto implements Serializable {
    private Long _id;
    @ApiModelProperty("创建时间")
    private Date createdAt;
    @ApiModelProperty("更新时间")
    private Date updatedAt;
    @ApiModelProperty("创建用户")
    private String createUserName;
    @ApiModelProperty("更新用户")
    private String updateUserName;
    @ApiModelProperty("处理用户")
    private String handleUserName;
    @ApiModelProperty("时间起")
    private Date startDate;
    @ApiModelProperty("时间止")
    private Date endDate;
    @ApiModelProperty("分页")
    private Pager pager;

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public String getHandleUserName() {
        return handleUserName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setHandleUserName(String handleUserName) {
        this.handleUserName = handleUserName;
    }

    public boolean validHandleUser(){
        return !(this.handleUserName.isEmpty());
    }
}
