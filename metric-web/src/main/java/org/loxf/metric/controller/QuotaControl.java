package org.loxf.metric.controller;

import com.alibaba.fastjson.JSON;
import org.loxf.metric.api.IQuotaService;
import org.loxf.metric.common.dto.QuotaDto;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.filter.LoginFilter;
import org.loxf.metric.service.impl.QuotaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by luohj on 2017/7/15.
 */
@Controller
@RequestMapping("/quota")
public class QuotaControl {
    @Autowired
    private IQuotaService quotaService;

    /**
     * 创建指标
     * @param quotaDto
     * @return
     */
    @RequestMapping("/testChartPage")
    @ResponseBody
    public String createQuota(QuotaDto quotaDto, HttpServletRequest request, HttpServletResponse response){
        return JSON.toJSONString(quotaService.insertItem(quotaDto));
    }
    /**
     * 删除指标
     * @return
     */
    @RequestMapping("/testChartPage")
    @ResponseBody
    public String createQuota(String quotaCode, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return JSON.toJSONString(quotaService.delItemByCode(quotaCode, userDto.getUserName()));
    }
}
