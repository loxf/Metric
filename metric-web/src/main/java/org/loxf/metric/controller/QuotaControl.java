package org.loxf.metric.controller;

import com.alibaba.fastjson.JSON;
import org.loxf.metric.api.IQuotaService;
import org.loxf.metric.common.dto.QuotaDto;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.filter.LoginFilter;
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
    @RequestMapping("/create")
    @ResponseBody
    public String createQuota(QuotaDto quotaDto, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        quotaDto.setHandleUserName(quotaDto.getHandleUserName());
        quotaDto.setUniqueCode(quotaDto.getUniqueCode());
        return JSON.toJSONString(quotaService.insertItem(quotaDto));
    }
    /**
     * 创建指标
     * @param quotaDto
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public String updateQuota(QuotaDto quotaDto, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        quotaDto.setHandleUserName(quotaDto.getHandleUserName());
        quotaDto.setUniqueCode(quotaDto.getUniqueCode());
        return JSON.toJSONString(quotaService.updateItem(quotaDto));
    }
    /**
     * 删除指标
     * @return
     */
    @RequestMapping("/remove")
    @ResponseBody
    public String rmQuota(String quotaCode, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return JSON.toJSONString(quotaService.delItemByCode(quotaCode, userDto.getUserName()));
    }
    /**
     * 获取单个指标
     * @return
     */
    @RequestMapping("/getQuota")
    @ResponseBody
    public String getQuota(String quotaCode, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return JSON.toJSONString(quotaService.queryItemByCode(quotaCode, userDto.getUserName()));
    }
    /**
     * 获取指标（分页）
     * @return
     */
    @RequestMapping("/pager")
    @ResponseBody
    public String pager(QuotaDto quotaDto, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        quotaDto.setHandleUserName(userDto.getUserName());
        quotaDto.setUniqueCode(userDto.getUniqueCode());
        return JSON.toJSONString(quotaService.getPageList(quotaDto));
    }
    /**
     * 获取指标（分页）
     * @return
     */
    @RequestMapping("/query")
    @ResponseBody
    public String queryAllQuota(QuotaDto quotaDto, HttpServletRequest request, HttpServletResponse response){
        return JSON.toJSONString(quotaService.queryQuotaList(quotaDto));
    }

}
