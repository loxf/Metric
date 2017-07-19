package org.loxf.metric.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.*;
import org.loxf.metric.api.IQuotaDimensionService;
import org.loxf.metric.base.annotations.Permission;
import org.loxf.metric.base.constants.PermissionType;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.QuotaDim;
import org.loxf.metric.common.dto.QuotaDimensionDto;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.dal.po.QuotaDimension;
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
    public BaseResult createDim(@ApiParam(value = "维度名称", required = true) String dimName,
                                HttpServletRequest request, HttpServletResponse response){
        UserDto user = LoginFilter.getUser(request);
        QuotaDimensionDto dimensionDto = new QuotaDimensionDto();
        dimensionDto.setHandleUserName(user.getUserName());
        dimensionDto.setUniqueCode(user.getUniqueCode());
        dimensionDto.setDimName(dimName);
        return quotaDimensionService.insertItem(dimensionDto);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "更新维度", notes = "只能更新维度名称，需要ROOT权限", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult updateDim(@ApiParam(value = "维度编码", required = true)String dimCode,
                                @ApiParam(value = "维度名称", required = true)String dimName,
                                HttpServletRequest request, HttpServletResponse response){
        UserDto user = LoginFilter.getUser(request);
        QuotaDimensionDto dimensionDto = new QuotaDimensionDto();
        dimensionDto.setDimName(dimName);
        dimensionDto.setDimName(dimCode);
        dimensionDto.setHandleUserName(user.getHandleUserName());
        dimensionDto.setUniqueCode(user.getUniqueCode());
        return quotaDimensionService.updateItem(dimensionDto);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseBody
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "删除维度", notes = "只能删除未使用的维度，需要ROOT权限", httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult rmDim(@ApiParam(value = "维度编码", required = true) String dimCode,
                            HttpServletRequest request, HttpServletResponse response){
        UserDto user = (UserDto)LoginFilter.getUser(request);
        return quotaDimensionService.delItemByCode(dimCode, user.getUniqueCode(), user.getUserName());
    }
}
