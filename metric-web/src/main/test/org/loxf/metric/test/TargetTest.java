package org.loxf.metric.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.loxf.metric.api.ITargetService;
import org.loxf.metric.base.ItemList.TargetItem;
import org.loxf.metric.common.dto.TargetDto;
import org.loxf.metric.test.core.JUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luohj on 2017/7/12.
 */
@RunWith(JUnit4ClassRunner.class)
@ActiveProfiles("rd")
@ContextConfiguration(locations = {"classpath*:root-test.xml"})
public class TargetTest {
    private static Logger logger = LoggerFactory.getLogger(TargetTest.class);
    private static final String uniqueCode = "LCUkdguGTKSjstlwLT983Nkkfux";
    @Autowired
    private ITargetService targetService;
    @Test
    public void queryTarget(){
        TargetDto targetDto = new TargetDto();
        TargetItem item = new TargetItem();
        item.setQuotaCode("QUOTA001");
        List<TargetItem> list = new ArrayList<>();
        list.add(item);
        targetDto.setItemList(list);
        targetDto.setTargetName("测试");
        targetDto.setTargetEndTime(new Date());
        logger.debug(JSON.toJSONString(targetService.queryTarget(targetDto)));
    }

}
