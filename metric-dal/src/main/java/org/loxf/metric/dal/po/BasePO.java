package org.loxf.metric.dal.po;

import com.sun.xml.internal.rngom.parse.host.Base;
import org.bson.types.ObjectId;
import org.loxf.metric.base.utils.DateUtil;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by hutingting on 2017/7/5.
 */
public class BasePO {
    private ObjectId _id;
    private Date createdAt;
    private Date updatedAt;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mi:ss");
        this.createdAt = sf.parse(createdAt);
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mi:ss");
        this.updatedAt = sf.parse(updatedAt);
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void handleDateToMongo(){
        this.createdAt=DateUtil.turnToMongoDate(this.createdAt);
        this.updatedAt=DateUtil.turnToMongoDate(this.updatedAt);
    }

    public void handleMongoDateToJava(){
        this.createdAt=DateUtil.mongoDateTurnToJavaDate(this.createdAt);
        this.updatedAt=DateUtil.mongoDateTurnToJavaDate(this.updatedAt);
    }

//    public void handleMongoDateForList(List<BasePO> list){
//        for(BasePO po:list){
//            po.handleMongoDateToJava();
//        }
//    }
}
