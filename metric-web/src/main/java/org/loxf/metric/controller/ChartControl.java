package org.loxf.metric.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.*;
import org.loxf.metric.api.IChartService;
import org.loxf.metric.base.constants.VisibleTypeEnum;
import org.loxf.metric.common.constants.UserTypeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.ChartDto;
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
 * Created by luohj on 2017/7/15.
 */
@Controller
@RequestMapping("/chart")
@Api(value = "chart", description = "图表操作")
public class ChartControl {
    @Autowired
    private IChartService chartService;

    /**
     * 创建图
     * @param chartDto
     * @return
     */
    @ApiOperation(value = "创建图", notes = "创建一个图", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS", response = BaseResult.class),
            @ApiResponse(code = 501, message = "参数不能为空", response = BaseResult.class),
            @ApiResponse(code = 502, message = "参数校验失败", response = BaseResult.class),
            @ApiResponse(code = 503, message = "没有数据", response = BaseResult.class),
            @ApiResponse(code = 504, message = "用户不存在", response = BaseResult.class),
            @ApiResponse(code = 505, message = "数据已存在", response = BaseResult.class),
            @ApiResponse(code = 509, message = "接口弃用", response = BaseResult.class),
            @ApiResponse(code = 300, message = "权限不足", response = BaseResult.class)})
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> createChart(@RequestBody @ApiParam(value = "待创建的图实体") ChartDto chartDto,
                                          HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        chartDto.setHandleUserName(userDto.getUserName());
        chartDto.setUniqueCode(userDto.getUniqueCode());
        return chartService.insertItem(chartDto);
    }
    /**
     * 删除图
     * @return
     */
    @ApiOperation(value = "删除图", notes = "删除一个图", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS", response = String.class) })
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseBody
    public String rmChart(@RequestBody @ApiParam(value = "图编码") String chartCode, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return JSON.toJSONString(chartService.delItemByCode(chartCode, userDto.getUserName()));
    }
    /**
     * 获取单个图
     * @return
     */
    @ApiOperation(value = "获取单个图的信息", notes = "获取单个图的信息", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS", response = String.class) })
    @RequestMapping("/getChart")
    @ResponseBody
    public String getChart(@RequestBody @ApiParam(value = "图编码") String chartCode, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return JSON.toJSONString(chartService.queryItemByCode(chartCode, userDto.getUserName()));
    }
    /**
     * 获取图（分页）
     * @return
     */
    @RequestMapping("/pager")
    @ResponseBody
    @ApiOperation(value = "获取图列表", notes = "分页", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS", response = String.class) })
    public String pager(@RequestBody @ApiParam(value = "图实体") ChartDto chartDto, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        chartDto.setHandleUserName(userDto.getUserName());
        chartDto.setUniqueCode(userDto.getUniqueCode());
        if(userDto.getUserType().equals(UserTypeEnum.CHILD.name())){
            chartDto.setVisibleType(VisibleTypeEnum.SPECIFICRANGE.name());
        }
        return JSON.toJSONString(chartService.getPageList(chartDto));
    }

}
