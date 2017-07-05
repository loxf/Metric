package org.loxf.metric.dal.po;

import org.bson.types.ObjectId;

import java.sql.Timestamp;

/**
 * Created by hutingting on 2017/7/5.
 */
public class BasePO {
    private ObjectId id;
    private Timestamp createdAt;
    private Timestamp updateAt;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }
}
