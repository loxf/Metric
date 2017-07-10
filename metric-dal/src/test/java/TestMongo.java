import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.collections.map.HashedMap;
import org.bson.Document;
import org.junit.Test;
import org.loxf.metric.base.utils.DateUtil;
import org.loxf.metric.dal.po.Chart;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

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
    public void test() {
//        Chart chart=new Chart();
//        chart.setChartCode("bb");
//        chart.setChartName("罗傻逼");
//        Map map=MapAndBeanTransUtils.transBean2Map(chart);
//        System.out.print(map);
//
    }

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
                System.out.println("mongoDate:"+date);
                System.out.println("java Date:"+DateUtil.mongoDateTurnToJavaDate(date));
                System.out.println("----------------------");
            }catch (Exception e){
                System.out.println("ex");
            }
        }

       // Query query=new Query(Criteria.where("onumber").is("002").and("cname").);

//        Document document=new Document();
//        document.put("name","htt6");
//        Date date=new Date();
//        System.out.println(date);
//        document.put("createdAt", DateUtil.turnToMongoDate(date));
//        collection.insertOne(document);

    }

}


