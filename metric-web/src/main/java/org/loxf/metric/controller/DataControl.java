package org.loxf.metric.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.loxf.metric.api.IBoardService;
import org.loxf.metric.api.IDataQueryService;
import org.loxf.metric.api.IIndexSettingService;
import org.loxf.metric.api.ITargetService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private ITargetService targetService;
    /**
     * 获取首页数据
     * @return
     */
    @RequestMapping(value = "/getIndexData", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取首页数据", notes = "获取首页数据", httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<List<ChartData>> getIndexData(@ApiParam(value = "开始时间", required = true) String startCircleTime,
                                                    @RequestBody @ApiParam(value = "结束时间", required = true) String endCircleTime,
                                                    HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        BaseResult result = indexSettingService.getIndexSetting(userDto.getUserName());
        if(result.getCode().equals(ResultCodeEnum.SUCCESS.getCode())){
            List<ChartItem> chartItemList = (List<ChartItem> )result.getData();
            if(CollectionUtils.isNotEmpty(chartItemList)){
                ConditionVo conditionVo = new ConditionVo();
                conditionVo.setStartCircleTime(startCircleTime);
                conditionVo.setEndCircleTime(endCircleTime);
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
                                                    @RequestBody @ApiParam(value = "查询条件", required = true) ConditionVo conditionVo,
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

    /**
     * 获取目标数据
     * @return
     */
    @RequestMapping(value = "/getTargetData", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取目标数据", notes = "获取目标数据", httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
        public BaseResult<List<TargetData>> getTargetData(@ApiParam(value = "目标名称", required = true) String targetName,
                                                     @ApiParam(value = "开始时间", required = true) Date startTime,
                                                     @ApiParam(value = "结束时间", required = true) Date endTime,
                                                    HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        TargetDto dto = new TargetDto();
        dto.setUniqueCode(userDto.getUniqueCode());
        dto.setHandleUserName(userDto.getUserName());
        dto.setTargetStartTime(startTime);
        dto.setTargetStartTime(endTime);
        BaseResult<List<TargetDto>> listBaseResult = targetService.queryTarget(dto);
        if (listBaseResult.getCode().equals(ResultCodeEnum.SUCCESS.getCode()) && CollectionUtils.isNotEmpty(listBaseResult.getData())) {
            List<TargetData> targetDataList = new ArrayList<>();
            for (TargetDto targetDto : listBaseResult.getData()) {
                TargetData targetData = dataQueryService.getTargetData(targetDto);
                targetDataList.add(targetData);
            }
            return new BaseResult<>(targetDataList);
        } else {
            return new BaseResult<>(listBaseResult.getCode(), listBaseResult.getMsg());
        }
    }
}
