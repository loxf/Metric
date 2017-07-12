package org.loxf.metric.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.Pager;
import org.loxf.metric.service.impl.ChartServiceImpl;
import org.loxf.metric.test.core.JUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by hutingting on 2017/7/12.
 */
@RunWith(JUnit4ClassRunner.class)
@ActiveProfiles("rd")
@ContextConfiguration(locations = {"classpath*:root-test.xml"})
public class ChartServiceTest {
    @Autowired
    private ChartServiceImpl chartService;
    @Test
    public void testgetPageList(){

        /*QuotaDto quotaDto = new QuotaDto();
        quotaDto.setHandleUserName("admin");
        quotaDto.setType(QuotaType.BASIC.getValue());
        Pager pager = new Pager();
        pager.setRownum(10);
        pager.setCurrentPage(1);
        quotaDto.setPager(pager);*/
        ChartDto obj=new ChartDto();
        Pager pager=new Pager();
        pager.setCurrentPage(1);
        pager.setRownum(10);
        obj.setPager(pager);
        obj.setHandleUserName("admin");
        PageData pageData=chartService.getPageList2(obj);
        int a=0;
    }
}
