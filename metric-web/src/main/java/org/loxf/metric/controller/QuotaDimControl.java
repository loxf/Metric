package org.loxf.metric.controller;

import com.alibaba.fastjson.JSON;
import org.loxf.metric.api.IQuotaDimensionService;
import org.loxf.metric.base.annotations.Permission;
import org.loxf.metric.base.constants.PermissionType;
import org.loxf.metric.common.dto.QuotaDim;
import org.loxf.metric.common.dto.QuotaDimensionDto;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by luohj on 2017/7/17.
 */
@Controller
@RequestMapping("/quotaDim")
public class QuotaDimControl {
    @Autowired
    private IQuotaDimensionService quotaDimensionService;

    @RequestMapping("/create")
    @ResponseBody
    @Permission(PermissionType.ROOT)
    public String createDim(QuotaDimensionDto dimensionDto, HttpServletRequest request, HttpServletResponse response){
        UserDto user = (UserDto)LoginFilter.getUser(request);
        dimensionDto.setHandleUserName(user.getUserName());
        return JSON.toJSONString(quotaDimensionService.insertItem(dimensionDto));
    }

    @RequestMapping("/update")
    @ResponseBody
    @Permission(PermissionType.ROOT)
    public String updateDim(QuotaDimensionDto dimensionDto, HttpServletRequest request, HttpServletResponse response){
        UserDto user = (UserDto)LoginFilter.getUser(request);
        dimensionDto.setHandleUserName(user.getUserName());
        return JSON.toJSONString(quotaDimensionService.updateItem(dimensionDto));
    }

    @RequestMapping("/remove")
    @ResponseBody
    @Permission(PermissionType.ROOT)
    public String rmDim(String dimCode, HttpServletRequest request, HttpServletResponse response){
        UserDto user = (UserDto)LoginFilter.getUser(request);
        return JSON.toJSONString(quotaDimensionService.delItemByCode(dimCode, user.getUniqueCode(), user.getUserName()));
    }
}
