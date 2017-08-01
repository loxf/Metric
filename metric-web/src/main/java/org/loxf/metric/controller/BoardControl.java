package org.loxf.metric.controller;

import io.swagger.annotations.*;
import org.loxf.metric.api.IBoardService;
import org.loxf.metric.base.ItemList.ChartItem;
import org.loxf.metric.base.annotations.Permission;
import org.loxf.metric.base.constants.PermissionType;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.BoardDto;
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
 * 
 * Created by luohj on 2017/7/15.
 */
@Controller
@RequestMapping("/board")
@Api(value = "board", description = "看板")
public class BoardControl {
    @Autowired
    private IBoardService boardService;

    /**
     * 创建看板
     * @param boardDto
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST,consumes = "application/json;charset=UTF-8")
    @ResponseBody
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "创建看板", notes = "创建一个看板，需要ROOT权限", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<String> createBoard(@RequestBody @ApiParam(value = "待创建的看板实体") BoardDto boardDto,
                                          HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        boardDto.setHandleUserName(userDto.getUserName());
        boardDto.setUniqueCode(userDto.getUniqueCode());
        return boardService.insertItem(boardDto);
    }
    /**
     * 删除看板
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseBody
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "删除看板", notes = "删除一个看板，需要ROOT权限", httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult rmBoard(@ApiParam(value = "看板编码") String boardCode, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return boardService.delItemByCode(boardCode, userDto.getUserName());
    }
    /**
     * 获取单个看板
     * @return
     */
    @RequestMapping(value = "/getBoard", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取看板的信息", notes = "获取看板的信息", httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<BoardDto> getBoard(@ApiParam(value = "看板编码") String boardCode, HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return boardService.queryItemByCode(boardCode, userDto);
    }
    /**
     * 获取看板（分页）
     * @return
     */
    @RequestMapping(value = "/pager", method = RequestMethod.POST,consumes = "application/json;charset=UTF-8")
    @ResponseBody   //缺测：子用户获取
    @ApiOperation(value = "获取看板列表", notes = "分页获取", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<PageData<BoardDto>> pager(@RequestBody @ApiParam(value = "看板实体") BoardDto boardDto,
                                      HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        boardDto.setHandleUserName(userDto.getUserName());
        boardDto.setUniqueCode(userDto.getUniqueCode());
        return boardService.getPageList(boardDto);
    }

    /**
     * 看板添加图
     * @return
     */
    @RequestMapping(value = "/addChartList", method = RequestMethod.POST,consumes = "application/json;charset=UTF-8")
    @ResponseBody   //缺测：子用户获取
    @Permission(PermissionType.ROOT)
    @ApiOperation(value = "添加图", notes = "添加图", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<String> addChartList(@ApiParam(value = "看板code") String boardCode,
                                           @RequestBody @ApiParam(value = "图列表") List<ChartItem> chartItemList,
                                                HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return boardService.addChartList(boardCode,userDto,chartItemList);
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
    public BaseResult<String> delChartList(@ApiParam(value = "看板code") String boardCode,
                                           @ApiParam(value = "图code列表，以逗号做分隔") String chartCodeList,
                                           HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = LoginFilter.getUser(request);
        return boardService.delChartList(boardCode,chartCodeList,userDto.getUserName());
    }
}
