package org.loxf.metric.controller;

import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.loxf.metric.base.constants.RateLimitType;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.Pager;
import org.loxf.metric.utils.SendMsgUtils;
import org.loxf.metric.api.IUserService;
import org.loxf.metric.base.annotations.Permission;
import org.loxf.metric.base.constants.PermissionType;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hutingting on 2017/7/17.
 */
@Controller
@RequestMapping("/user")
@Api(value = "user", description = "用户管理")
public class UserController {

    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private IUserService userService;

    /**
     * 添加子用户
     *
     * @return
     */
    @Permission(PermissionType.ROOT)
    @RequestMapping(value = "/addChildUser", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "新增子用户", notes = "需要ROOT权限",
            httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<String> addChildUser(@ApiParam(value = "团队码", required = true) String uniqueCode,
                                           @ApiParam(value = "手机", required = true) String phone,
                                           @ApiParam(value = "邮箱", required = true) String email,
                                           @ApiParam(value = "真实姓名", required = true) String realName,
                                           HttpServletRequest request, HttpServletResponse response) {
        UserDto userDto = new UserDto();
        userDto.setUniqueCode(uniqueCode);
        userDto.setRealName(realName);
        userDto.setEmail(email);
        userDto.setPhone(phone);
        BaseResult<String> addChildUserResult=userService.addChildUser(userDto);
        if(addChildUserResult.isSucess()){//用户添加成功
            //待改造
            //向子用户发送短信
            SendMsgUtils.sendMsgByType(RateLimitType.CHILDSENDPWDCODE, phone);

        }
        return addChildUserResult;

    }

    /**
     * 注销子用户
     *
     * @param childUserName
     * @param request
     * @param response
     * @return
     */
    @Permission(PermissionType.ROOT)
    @RequestMapping(value = "/disableChildUser", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "注销子用户", notes = "需要ROOT权限",
            httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<String> disableChildUser(@ApiParam(value = "子用户名", required = true) String childUserName, HttpServletRequest request, HttpServletResponse response) {
        UserDto rootUser = LoginFilter.getUser(request);
        return userService.disableChildUser(childUserName, rootUser.getUserName(), rootUser.getUniqueCode());
    }

    /**
     * 修改密码
     *
     * @param oldPwd
     * @param newPwd
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/modifyPwd", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "修改密码", notes = "修改密码，通过老密码修改",
            httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    @Permission
    public BaseResult<String> modifyPwd(@RequestParam String oldPwd, @RequestParam String newPwd, HttpServletRequest request, HttpServletResponse response) {
        UserDto existsUser = LoginFilter.getUser(request);
        return userService.modifyPwd(existsUser, oldPwd, newPwd);
    }

    /**
     * 获取团队中的用户列表
     *
     * @return
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取团队中的用户列表", notes = "获取团队中的用户列表，包括自身",
            httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    @Permission
    public BaseResult<PageData<UserDto>>  getUserList(@ApiParam(value = "页码", required = false) String pageNum,@ApiParam(value = "页数", required = false) String pageSize,HttpServletRequest request) {
        UserDto existsUser = LoginFilter.getUser(request);
        Pager pager=new Pager();
        if(StringUtils.isNotBlank(pageSize)){
            pager.setRownum(Integer.parseInt(pageSize));
        }
        if(StringUtils.isNotBlank(pageNum)){
            pager.setCurrentPage(Integer.parseInt(pageNum));
        }
        existsUser.setPager(pager);
        return userService.getPageList(existsUser);
    }

}
