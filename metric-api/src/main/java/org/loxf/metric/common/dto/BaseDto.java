package org.loxf.metric.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hutingting on 2017/7/6.
 */
@ApiModel("通用数据")
public class BaseDto implements Serializable {
    @ApiModelProperty(value = "ID", hidden = true)
    private Long _id;
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createdAt;
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Date updatedAt;
    @ApiModelProperty(value = "创建用户", hidden = true)
    private String createUserName;
    @ApiModelProperty(value = "更新用户", hidden = true)
    private String updateUserName;
    @ApiModelProperty(value = "处理用户", required = true)
    private String handleUserName;
    @ApiModelProperty(value = "时间起", hidden = true)
    private Date startDate;
    @ApiModelProperty(value = "时间止", hidden = true)
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
