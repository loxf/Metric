package org.loxf.metric.common.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hutingting on 2017/7/6.
 */
public class BaseDto implements Serializable {
    private Long _id;
    private Date createdAt;
    private Date updatedAt;

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
}
