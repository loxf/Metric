package org.loxf.metric.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.loxf.metric.api.IChartService;
import org.loxf.metric.base.constants.VisibleTypeEnum;
import org.loxf.metric.common.constants.UserTypeEnum;
import org.loxf.metric.common.dto.ChartDto;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "创建图", notes = "创建一个图", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS", response = String.class) })
    public String createChart(ChartDto chartDto, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        chartDto.setHandleUserName(userDto.getUserName());
        chartDto.setUniqueCode(userDto.getUniqueCode());
        return JSON.toJSONString(chartService.insertItem(chartDto));
    }
    /**
     * 删除图
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "删除图", notes = "删除一个图", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS", response = String.class) })
    public String rmChart(String quotaCode, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return JSON.toJSONString(chartService.delItemByCode(quotaCode, userDto.getUserName()));
    }
    /**
     * 获取单个图
     * @return
     */
    @RequestMapping("/getChart")
    @ResponseBody
    @ApiOperation(value = "获取单个图的信息", notes = "获取单个图的信息", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS", response = String.class) })
    public String getChart(String chartQuota, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return JSON.toJSONString(chartService.queryItemByCode(chartQuota, userDto.getUserName()));
    }
    /**
     * 获取图（分页）
     * @return
     */
    @RequestMapping("/pager")
    @ResponseBody
    @ApiOperation(value = "获取图列表", notes = "分页", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "SUCCESS", response = String.class) })
    public String pager(ChartDto chartDto, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        chartDto.setHandleUserName(userDto.getUserName());
        chartDto.setUniqueCode(userDto.getUniqueCode());
        if(userDto.getUserType().equals(UserTypeEnum.CHILD.name())){
            chartDto.setVisibleType(VisibleTypeEnum.SPECIFICRANGE.name());
        }
        return JSON.toJSONString(chartService.getPageList(chartDto));
    }

}
