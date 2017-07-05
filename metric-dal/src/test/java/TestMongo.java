import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;
import org.loxf.metric.base.utils.DateUtil;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;


/**
 * Created by hutingting on 2017/7/5.
 */
public class TestMongo {
    @Test
    public void testMongodb() {
        //地址
        ServerAddress addr = new ServerAddress("106.14.138.123", 27017);
        //权限验证
        MongoCredential credential = MongoCredential.createCredential(
                "metric", "test-metric", "metric".toCharArray());
        //参数设置
        MongoClientOptions options = MongoClientOptions.builder()
                .serverSelectionTimeout(1000)
                .build();
        MongoClient mongoClient = new MongoClient(addr, Arrays.asList(credential), options);

        MongoDatabase db = mongoClient.getDatabase("test-metric");
        MongoCollection<Document> collection= db.getCollection("user");
        FindIterable<Document> list= collection.find();
        for(Document document:list){
            try{
                Date date= document.getDate("createdAt");
                System.out.println(DateUtil.mongoDateTurnToJavaDate(date));
            }catch (Exception e){
                System.out.println("ex");
            }
        }

//        Document document=new Document();
//        document.put("name","htt6");
//        Date date=new Date();
//        System.out.println(date);
//        document.put("createdAt", DateUtil.turnToMongoDate(date));
//        collection.insertOne(document);

    }

}


