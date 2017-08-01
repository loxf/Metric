package org.loxf.metric.controller;

import io.swagger.annotations.*;
import org.loxf.metric.api.ITargetService;
import org.loxf.metric.base.annotations.Permission;
import org.loxf.metric.base.constants.PermissionType;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.TargetDto;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.dal.po.Target;
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
 * 
 * Created by luohj on 2017/7/15.
 */
@Controller
@RequestMapping("/target")
@Api(value = "target", description = "目标")
public class TargetControl {
    @Autowired
    private ITargetService targetService;

    /**
     * 创建目标
     * @param targetDto
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "创建目标", notes = "创建一个目标，需要ROOT权限", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<String> createTarget(@RequestBody @ApiParam(value = "待创建的目标实体") TargetDto targetDto,
                                          HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        targetDto.setHandleUserName(userDto.getUserName());
        targetDto.setUniqueCode(userDto.getUniqueCode());
        return targetService.insertItem(targetDto);
    }
    /**
     * 删除目标
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseBody
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "删除目标", notes = "删除一个目标，需要ROOT权限", httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult rmTarget(@ApiParam(value = "目标编码") String targetCode, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return targetService.delItemByCode(targetCode, userDto.getUserName());
    }
    /**
     * 获取单个目标
     * @return
     */
    @RequestMapping(value = "/getTarget", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取目标的信息", notes = "获取目标的信息", httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<TargetDto> getTarget(@ApiParam(value = "目标编码") String targetCode, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return targetService.queryItemByCode(targetCode,userDto);
    }
    /**
     * 获取目标（分页）
     * @return
     */
    @RequestMapping(value = "/pager", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "获取目标列表", notes = "分页获取", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<PageData<TargetDto>> pager(@RequestBody @ApiParam(value = "目标实体") TargetDto targetDto,
                                              HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        targetDto.setHandleUserName(userDto.getUserName());
        targetDto.setUniqueCode(userDto.getUniqueCode());
        return targetService.getPageList(targetDto);
    }

}
