package org.loxf.metric.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.loxf.metric.api.IQuotaDimensionService;
import org.loxf.metric.base.annotations.Permission;
import org.loxf.metric.base.constants.PermissionType;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.QuotaDim;
import org.loxf.metric.common.dto.QuotaDimensionDto;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by luohj on 2017/7/17.
 */
@Controller
@RequestMapping("/quotaDim")
@Api(value = "quotaDim", description = "指标维度")
public class QuotaDimControl {
    @Autowired
    private IQuotaDimensionService quotaDimensionService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "创建维度", notes = "定义一个新的维度，需要ROOT权限", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult createDim(@RequestBody QuotaDimensionDto dimensionDto, HttpServletRequest request, HttpServletResponse response){
        UserDto user = (UserDto)LoginFilter.getUser(request);
        dimensionDto.setHandleUserName(user.getUserName());
        return quotaDimensionService.insertItem(dimensionDto);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "更新维度", notes = "只能更新维度名称，需要ROOT权限", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult updateDim(@RequestBody QuotaDimensionDto dimensionDto, HttpServletRequest request, HttpServletResponse response){
        UserDto user = (UserDto)LoginFilter.getUser(request);
        dimensionDto.setHandleUserName(user.getUserName());
        return quotaDimensionService.updateItem(dimensionDto);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "删除维度", notes = "只能删除未使用的维度，需要ROOT权限", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult rmDim(@RequestBody String dimCode, HttpServletRequest request, HttpServletResponse response){
        UserDto user = (UserDto)LoginFilter.getUser(request);
        return quotaDimensionService.delItemByCode(dimCode, user.getUniqueCode(), user.getUserName());
    }
}
