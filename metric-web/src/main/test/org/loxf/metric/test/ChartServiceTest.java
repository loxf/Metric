package org.loxf.metric.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.loxf.metric.base.constants.VisibleTypeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.Pager;
import org.loxf.metric.service.impl.ChartServiceImpl;
import org.loxf.metric.test.core.JUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.event.TransactionalEventListener;

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
    public  void testAddChart(){
        ChartDto chartDto=new ChartDto();
        BaseResult<String> result=chartService.insertItem(chartDto);
        System.out.println("resultCode:"+result.getCode());
        System.out.println("resultMsg:"+result.getMsg());
        System.out.println("resultData:"+result.getData());
    }

    @Test
    public void testgetPageList(){
        ChartDto obj=new ChartDto();
        Pager pager=new Pager();
        pager.setCurrentPage(1);
        pager.setRownum(10);
        obj.setPager(pager);
        obj.setHandleUserName("admin");
        obj.setVisibleType(VisibleTypeEnum.SPECIFICRANGE.name());
        //obj.set
        BaseResult<PageData> i=chartService.getPageList(obj);
        int a=0;
    }


}
