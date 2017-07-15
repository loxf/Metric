package org.loxf.metric.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.loxf.metric.api.IDataQueryService;
import org.loxf.metric.common.dto.ConditionVo;
import org.loxf.metric.test.core.JUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by luohj on 2017/7/15.
 */
@RunWith(JUnit4ClassRunner.class)
@ActiveProfiles("rd")
@ContextConfiguration(locations = {"classpath*:root-test.xml"})
public class DataQueryServiceTest {
    private static Logger logger = LoggerFactory.getLogger(DataQueryServiceTest.class);
    @Autowired
    private IDataQueryService dataQueryService;

    @Test
    public void queryChart(){
        ConditionVo conditionVo = new ConditionVo();
        conditionVo.setStartCircleTime("2017-06-01");
        conditionVo.setEndCircleTime("2017-07-30");
        logger.debug(JSON.toJSONString(dataQueryService.getChartData(
                "admin","CHART001", conditionVo)));
    }
    @Test
    public void queryBoard(){
        ConditionVo conditionVo = new ConditionVo();
        conditionVo.setStartCircleTime("2017-06-01");
        conditionVo.setEndCircleTime("2017-07-30");
        logger.debug(JSON.toJSONString(dataQueryService.getBoardData(
                "admin","BOARD001", conditionVo)));
    }
}
