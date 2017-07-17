package org.loxf.metric.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.loxf.metric.api.IChartService;
import org.loxf.metric.base.constants.VisibleTypeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.Pager;
import org.loxf.metric.test.core.JUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by hutingting on 2017/7/12.
 */
@RunWith(JUnit4ClassRunner.class)
@ActiveProfiles("rd")
@ContextConfiguration(locations = {"classpath*:root-test.xml"})
public class ChartTest {
    private static Logger logger = LoggerFactory.getLogger(ChartTest.class);
    @Autowired
    private IChartService chartService;

    @Test
    public void testAddChart() {
        ChartDto chartDto = new ChartDto();
        BaseResult<String> result = chartService.insertItem(chartDto);
        logger.debug("获取结果：" + JSON.toJSONString(result));
    }

    @Test
    public void testgetPageList() {
        ChartDto obj = new ChartDto();
        Pager pager = new Pager();
        pager.setCurrentPage(1);
        pager.setRownum(10);
        obj.setPager(pager);
        obj.setHandleUserName("03308174361HGabcwUEGjGOTRrXuEUN1");
        //obj.set
        logger.debug("获取结果：" + JSON.toJSONString(chartService.getPageList(obj)));
    }

    @Test
    public void rmChart() {
        BaseResult<String> result = chartService.delItemByCode("", "03308174361HGabcwUEGjGOTRrXuEUN1");
        logger.debug("获取结果：" + JSON.toJSONString(result));
    }
}
