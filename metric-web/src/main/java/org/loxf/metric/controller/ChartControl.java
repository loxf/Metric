package org.loxf.metric.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.*;
import org.loxf.metric.api.IChartService;
import org.loxf.metric.base.annotations.Permission;
import org.loxf.metric.base.constants.PermissionType;
import org.loxf.metric.base.constants.VisibleTypeEnum;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.constants.UserTypeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by luohj on 2017/7/15.
 */
@Controller
@RequestMapping("/chart")
@Api(value = "chart", description = "图表")
public class ChartControl {
    @Autowired
    private IChartService chartService;

    /**
     * 创建图
     * @param chartDto
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "创建图", notes = "创建一个图，需要ROOT权限", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<String> createChart(@RequestBody @ApiParam(value = "待创建的图实体") ChartDto chartDto,
                                          HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        chartDto.setHandleUserName(userDto.getUserName());
        chartDto.setUniqueCode(userDto.getUniqueCode());
        chartDto.setCreateUserName(userDto.getUserName());
        return chartService.insertItem(chartDto);
    }
    /**
     * 删除图
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseBody
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "删除图", notes = "删除一个图，需要ROOT权限", httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult rmChart(@ApiParam(value = "图编码") String chartCode, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return chartService.delItemByCode(chartCode, userDto.getUserName());
    }
    /**
     * 获取单个图
     * @return
     */
    @RequestMapping(value = "/getChart", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取图的信息", notes = "获取图的信息", httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<ChartDto> getChart(@ApiParam(value = "图编码") String chartCode, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return chartService.queryItemByCode(chartCode, userDto);
    }
    /**
     * 获取图（分页）
     * @return
     */
    @RequestMapping(value = "/pager", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "获取图列表", notes = "分页获取", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<PageData<ChartDto>> pager(@RequestBody @ApiParam(value = "过滤图的条件") ChartDto chartDto, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        chartDto.setHandleUserName(userDto.getUserName());
        chartDto.setUniqueCode(userDto.getUniqueCode());
        if(userDto.getUserType().equals(UserTypeEnum.CHILD.name())){
            chartDto.setVisibleType(VisibleTypeEnum.SPECIFICRANGE.name());
        }
        return chartService.getPageList(chartDto);
    }

    /**
     * 获取图（分页，排除指定的图）
     * @return
     */
    @RequestMapping(value = "/getChartList", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "获取图列表", notes = "分页获取，排除指定的图", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<PageData<ChartDto>> getChartListExcludeChartList(
            @RequestBody @ApiParam(value = "过滤图的条件") ChartDto chartDto,
            @RequestBody @ApiParam(value = "要排除的图") List<String> chartList,
            HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        chartDto.setHandleUserName(userDto.getUserName());
        chartDto.setUniqueCode(userDto.getUniqueCode());
        if(userDto.getUserType().equals(UserTypeEnum.CHILD.name())){
            chartDto.setVisibleType(VisibleTypeEnum.SPECIFICRANGE.name());
        }
        return chartService.getPageListExcludeChartList(chartDto, chartList);
    }

}
