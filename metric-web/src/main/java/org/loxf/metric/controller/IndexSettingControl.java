package org.loxf.metric.controller;

import io.swagger.annotations.*;
import org.loxf.metric.api.IIndexSettingService;
import org.loxf.metric.base.ItemList.ChartItem;
import org.loxf.metric.base.annotations.Permission;
import org.loxf.metric.base.constants.PermissionType;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.*;
import org.loxf.metric.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 
 * Created by luohj on 2017/7/15.
 */
@Controller
@RequestMapping("/index")
@Api(value = "index", description = "首页配置")
public class IndexSettingControl {
    @Autowired
    private IIndexSettingService indexSettingService;

    /**
     * 看板添加图
     * @return
     */
    @RequestMapping(value = "/addChartList", method = RequestMethod.POST,consumes = "application/json;charset=UTF-8")
    @ResponseBody   //缺测：子用户获取
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "添加图", notes = "添加图", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<String> addChartList(@ApiParam(value = "首页看板code") String indexCode,
                                           @RequestBody @ApiParam(value = "图列表") List<ChartItem> chartItemList,
                                           HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return indexSettingService.addChartList(indexCode,userDto,chartItemList);
    }

    /**
     * 看板删除图
     * @return
     */
    @RequestMapping(value = "/delChartList", method = RequestMethod.GET,consumes = "application/json;charset=UTF-8")
    @ResponseBody   //缺测：子用户获取
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "删除图", notes = "删除图", httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<String> delChartList(@ApiParam(value = "首页看板code") String indexCode,
                                           @ApiParam(value = "图code列表，以逗号做分隔") String chartCodeList,
                                           HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return indexSettingService.delChartList(indexCode,userDto,chartCodeList);
    }

}
