package org.loxf.metric.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.loxf.metric.api.IQuotaService;
import org.loxf.metric.common.constants.QuotaType;
import org.loxf.metric.common.constants.StandardState;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.QuotaDimensionDto;
import org.loxf.metric.common.dto.QuotaDto;
import org.loxf.metric.dal.po.QuotaDimension;
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
@RunWith(SpringJUnit4ClassRunner.class)
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
        for(int i=1;i<=10;i++) {
            quotaDto.setQuotaSource("quota_data_test_00" + i);
            quotaDto.setExpression("${test_00" + i + "}");
            quotaDto.setQuotaSource("自动测试指标" + i);
            quotaDto.setType(QuotaType.BASIC.getValue());
            quotaDto.setShowOperation("SUM");
            quotaDto.setState(StandardState.AVAILABLE.getValue());
            List<QuotaDimensionDto> quotaDimensionList = new ArrayList<>();
            QuotaDimensionDto dim1 = new QuotaDimensionDto();
            dim1.setDimCode("productName");
            dim1.setDimName("产品名称");
            QuotaDimensionDto dim2 = new QuotaDimensionDto();
            dim2.setDimCode("productAddr");
            dim2.setDimName("产地");
            quotaDimensionList.add(dim1);
            quotaDimensionList.add(dim2);
            quotaDto.setQuotaDim(quotaDimensionList);
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
        BaseResult<QuotaDto> result = quotaService.queryItemByCode("QUOTA001");
        logger.debug(JSON.toJSONString(result));
    }

    @Test
    public void delQuota(){
        BaseResult<String> result = quotaService.delItemByCode("QUOTA_00000001499743845561158610");
        logger.debug(JSON.toJSONString(result));
    }
}
