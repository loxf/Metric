package org.loxf.metric.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.loxf.metric.api.IQuotaService;
import org.loxf.metric.base.annotations.Permission;
import org.loxf.metric.base.constants.PermissionType;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.QuotaDto;
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
import java.util.List;

/**
 * Created by luohj on 2017/7/15.
 */
@Controller
@RequestMapping("/quota")
@Api(value = "quota", description = "指标")
public class QuotaControl {
    @Autowired
    private IQuotaService quotaService;

    /**
     * 创建指标
     * @param quotaDto
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "创建指标", notes = "定义一个新的指标，需要ROOT权限", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult createQuota(@RequestBody QuotaDto quotaDto, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        quotaDto.setHandleUserName(quotaDto.getHandleUserName());
        quotaDto.setUniqueCode(quotaDto.getUniqueCode());
        return quotaService.insertItem(quotaDto);
    }
    /**
     * 创建指标
     * @param quotaDto
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "更新指标", notes = "复合指标的所有内容都能修改，基础指标只能修改：展示类型（数量/金额/比例），展现方式（合计/最大值/最小值/平均值/计数），数据接入方式（SDK/EXCEL），需要ROOT权限",
            httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult updateQuota(@RequestBody QuotaDto quotaDto, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        quotaDto.setHandleUserName(quotaDto.getHandleUserName());
        quotaDto.setUniqueCode(quotaDto.getUniqueCode());
        return quotaService.updateItem(quotaDto);
    }
    /**
     * 删除指标
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "删除指标", notes = "如果指标被其他指标引用，不能删除。如果被有效目标（当前日期在目标的时间范围）引用，不能删除。需要ROOT权限",
            httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult rmQuota(@RequestBody String quotaCode, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return quotaService.delItemByCode(quotaCode, userDto.getUserName());
    }
    /**
     * 获取单个指标
     * @return
     */
    @RequestMapping("/getQuota")
    @ResponseBody
    @ApiOperation(value = "获取指标", notes = "获取一个指标", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult getQuota(@RequestBody String quotaCode, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return quotaService.queryItemByCode(quotaCode, userDto.getUserName());
    }
    /**
     * 获取指标（分页）
     * @return
     */
    @RequestMapping("/pager")
    @ResponseBody
    @ApiOperation(value = "获取指标（分页）", notes = "获取分页指标列表", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<PageData> pager(@RequestBody QuotaDto quotaDto, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        quotaDto.setHandleUserName(userDto.getUserName());
        quotaDto.setUniqueCode(userDto.getUniqueCode());
        return quotaService.getPageList(quotaDto);
    }
    /**
     * 获取指标
     * @return
     */
    @RequestMapping("/query")
    @ResponseBody
    @ApiOperation(value = "获取指标列表", notes = "获取指标列表", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<List<QuotaDto>> queryAllQuota(@RequestBody QuotaDto quotaDto,
                                                    HttpServletRequest request, HttpServletResponse response){
        return quotaService.queryQuotaList(quotaDto);
    }

}
