package org.loxf.metric.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.loxf.metric.api.IDataImportService;
import org.loxf.metric.base.utils.RandomUtils;
import org.loxf.metric.test.core.JUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by luohj on 2017/7/13.
 */
@RunWith(JUnit4ClassRunner.class)
@ActiveProfiles("rd")
@ContextConfiguration(locations = {"classpath*:root-test.xml"})
public class DataTest {
    private static Logger logger = LoggerFactory.getLogger(QuotaTest.class);
    @Autowired
    private IDataImportService dataService;
    @Test
    public void importData() throws ParseException {
        List<Map> data = new ArrayList<>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0; i<100; i++){
            Map map = new HashMap();
            map.put("value", RandomUtils.getRandom());
            map.put("circleTime", sf.parse("2017-01-01 00:00:00"));
            map.put("productName", "产品"+i);
            data.add(map);
        }
        logger.debug(JSON.toJSONString(dataService.importData("admin", "QUOTA_NenEUV1", data)));
    }

    @Test
    public void rmDataByCircleTime() throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.debug(JSON.toJSONString(dataService.rmDataByCircleTime("admin",
                "QUOTA_NenEUV1", sf.parse("2017-01-01 00:00:00"))));
    }

    @Test
    public void dropData() throws ParseException {
        logger.debug(JSON.toJSONString(dataService.dropAllData("admin",
                "QUOTA_NenEUV1")));
    }
}
