package org.loxf.metric.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.loxf.metric.api.IQuotaService;
import org.loxf.metric.base.ItemList.QuotaDimItem;
import org.loxf.metric.common.constants.*;
import org.loxf.metric.common.dto.*;
import org.loxf.metric.dal.po.Quota;
import org.loxf.metric.dal.po.QuotaDimension;
import org.loxf.metric.test.core.JUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luohj on 2017/7/10.
 */
@RunWith(JUnit4ClassRunner.class)
@ActiveProfiles("rd")
@ContextConfiguration(locations = {"classpath*:root-test.xml"})
public class QuotaTest {
    private static Logger logger = LoggerFactory.getLogger(QuotaTest.class);
    private static final String uniqueCode = "LCUkdguGTKSjstlwLT983Nkkfux";
    @Autowired
    private IQuotaService quotaService;
    @Test
    public void create(){
        QuotaDto quotaDto = new QuotaDto();
        quotaDto.setQuotaName("自动测试指标");
        quotaDto.setType(QuotaType.BASIC.getValue());
        quotaDto.setShowOperation(SummaryType.SUM.name());
        quotaDto.setState(StandardState.AVAILABLE.getValue());
        List<QuotaDimItem> quotaDimItemList = new ArrayList<>();
        QuotaDimItem dim1 = new QuotaDimItem();
        dim1.setDimCode("productName");
        dim1.setDimName("产品名称");
        QuotaDimItem dim2 = new QuotaDimItem();
        dim2.setDimCode("productAddr");
        dim2.setDimName("产地");
        quotaDimItemList.add(dim1);
        quotaDimItemList.add(dim2);
        quotaDto.setQuotaDim(quotaDimItemList);
        quotaDto.setShowType(ShowType.MONEY.name());
        quotaDto.setUniqueCode(uniqueCode);
        quotaDto.setIntervalPeriod(1440);
        quotaDto.setHandleUserName("admin");
        quotaDto.setDataImportType(DataImportType.EXCEL.name());
        BaseResult<String> result = quotaService.insertItem(quotaDto);
        logger.debug("获取结果：" + JSON.toJSONString(result));
    }

    @Test
    public void getQuota(){
        QuotaDto quotaDto = new QuotaDto();
        BaseResult<QuotaDto> result = quotaService.queryItemByCode("QUOTA001", "admin");
        logger.debug("获取结果：" + JSON.toJSONString(result));
    }

    @Test
    public void updateQuota(){
        QuotaDto quotaDto = new QuotaDto();
        quotaDto.setQuotaCode("QUOTA001");
        quotaDto.setType(QuotaType.BASIC.getValue());
        quotaDto.setQuotaSource("q_d_QUOTATEST");
        quotaDto.setShowType(ShowType.MONEY.getValue());
        quotaDto.setDataImportType(DataImportType.SDK.name());
        BaseResult<String> result = quotaService.updateItem(quotaDto);
        logger.debug("获取结果：" + JSON.toJSONString(result));
    }

    @Test
    public void delQuota(){
        BaseResult<String> result = quotaService.delItemByCode("QUOTA_00000001499743845968266918", "admin");
        logger.debug("获取结果：" + JSON.toJSONString(result));
    }

    @Test
    public void getPageList(){
        QuotaDto quotaDto = new QuotaDto();
        quotaDto.setHandleUserName("admin");
        quotaDto.setType(QuotaType.BASIC.getValue());
        Pager pager = new Pager();
        pager.setRownum(10);
        pager.setCurrentPage(1);
        quotaDto.setPager(pager);
        BaseResult<PageData> pageData = quotaService.getPageList(quotaDto);
        logger.debug("获取结果：" + JSON.toJSONString(pageData));
    }

    @Test
    public void checkDependcy(){
        logger.debug("获取结果：" + JSON.toJSONString(quotaService.checkDependencyQuota("QUOTA001")));
    }

    @Test
    public void queryList(){
        QuotaDimItem quotaDimItem = new QuotaDimItem();
        quotaDimItem.setDimCode("productName");
        List<QuotaDimItem> quotaDimItemList = new ArrayList<>();
        quotaDimItemList.add(quotaDimItem);

        QuotaDto quotaDto = new QuotaDto();
        quotaDto.setQuotaDim(quotaDimItemList);
        quotaDto.setQuotaName("指标");
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = sf.parse("2017-07-10 00:00:00");
            Date end = sf.parse("2017-07-13 23:59:59");
            quotaDto.setStartDate(start);
            quotaDto.setEndDate(end);
        } catch (ParseException e){
            logger.error("时间解析错误", e);
        }
        logger.debug("获取结果：" + JSON.toJSONString(quotaService.queryQuotaList(quotaDto)));
    }
}
