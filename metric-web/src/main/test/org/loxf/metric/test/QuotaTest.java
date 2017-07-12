package org.loxf.metric.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.loxf.metric.api.IQuotaService;
import org.loxf.metric.base.ItemList.QuotaDimItem;
import org.loxf.metric.common.constants.*;
import org.loxf.metric.common.dto.*;
import org.loxf.metric.dal.po.QuotaDimension;
import org.loxf.metric.test.core.JUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
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
        for(int i=1;i<=2;i++) {
            quotaDto.setQuotaSource("quota_data_test_00" + i);
            quotaDto.setQuotaName("自动测试指标" + i);
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
            quotaDto.setCreateUserName("admin");
            quotaDto.setUpdateUserName("admin");
            BaseResult<String> result = quotaService.insertItem(quotaDto);
            logger.debug(JSON.toJSONString(result));
        }
    }

    @Test
    public void getQuota(){
        QuotaDto quotaDto = new QuotaDto();
        BaseResult<QuotaDto> result = quotaService.queryItemByCode("QUOTA001", "admin");
        logger.debug(JSON.toJSONString(result));
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
        logger.debug(JSON.toJSONString(result));
    }

    @Test
    public void delQuota(){
        BaseResult<String> result = quotaService.delItemByCode("QUOTA_00000001499743845968266918", "admin");
        logger.debug(JSON.toJSONString(result));
    }

    @Test
    public void getPageList(){
        QuotaDto quotaDto = new QuotaDto();
        quotaDto.setType(QuotaType.BASIC.getValue());
        Pager pager = new Pager();
        pager.setRownum(10);
        pager.setCurrentPage(1);
        quotaDto.setPager(pager);
        PageData pageData = quotaService.getPageList(quotaDto);
        logger.debug(JSON.toJSONString(pageData));
    }

    @Test
    public void checkDependcy(){
        logger.debug(JSON.toJSONString(quotaService.checkDependencyQuota("QUOTA001")));
    }
}
