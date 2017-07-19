package org.loxf.metric.controller;

import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.loxf.metric.api.IBoardService;
import org.loxf.metric.api.IDataQueryService;
import org.loxf.metric.api.IIndexSettingService;
import org.loxf.metric.base.ItemList.ChartItem;
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
 * Created by luohj on 2017/7/19.
 */
@Controller
@RequestMapping("/data")
@Api(value = "data", description = "数据")
public class DataControl {
    @Autowired
    private IDataQueryService dataQueryService;
    @Autowired
    private IIndexSettingService indexSettingService;
    @Autowired
    private IBoardService boardService;
    /**
     * 获取首页数据
     * @return
     */
    @RequestMapping(value = "/getIndexData", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取首页数据", notes = "获取首页数据", httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<List<ChartData>> getIndexData(@ApiParam(value = "开始时间", required = true) String startDate,
                                                    @RequestBody @ApiParam(value = "结束时间", required = true) String endDate,
                                                    HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        BaseResult result = indexSettingService.getIndexSetting(userDto.getUserName());
        if(result.getCode().equals(ResultCodeEnum.SUCCESS.getCode())){
            List<ChartItem> chartItemList = (List<ChartItem> )result.getData();
            if(CollectionUtils.isNotEmpty(chartItemList)){
                ConditionVo conditionVo = new ConditionVo();
                conditionVo.setStartCircleTime(startDate);
                conditionVo.setEndCircleTime(endDate);
                List<ChartData> chartDataList = dataQueryService.getIndexData(userDto.getHandleUserName(), userDto.getUniqueCode(), conditionVo);
                return new BaseResult<>(chartDataList);
            }
        }
        return result;
    }

    /**
     * 获取看板数据
     * @return
     */
    @RequestMapping(value = "/getBoardData", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取看板数据", notes = "获取看板数据", httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<List<ChartData>> getBoardData(@ApiParam(value = "看板编码", required = true) String boardCode,
                                                    @RequestBody @ApiParam(value = "查询条件", required = false) ConditionVo conditionVo,
                                                    HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        BaseResult result = indexSettingService.getIndexSetting(userDto.getUniqueCode());
        if(result.getCode().equals(ResultCodeEnum.SUCCESS.getCode())){
            List<ChartItem> chartItemList = (List<ChartItem> )result.getData();
            if(CollectionUtils.isNotEmpty(chartItemList)){
                List<ChartData> chartDataList = dataQueryService.getIndexData(userDto.getHandleUserName(), userDto.getUniqueCode(), conditionVo);
                return new BaseResult<>(chartDataList);
            }
        }
        return result;
    }

}