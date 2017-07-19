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
     * 更新首页配置
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "更新首页配置", notes = "更新首页配置，需要ROOT权限", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult updateSetting(@RequestBody @ApiParam(value = "首页配置图列表")List<ChartItem> chartItemList,
                                    HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return indexSettingService.updateSetting(chartItemList, userDto.getUserName(), userDto.getUniqueCode());
    }

}
