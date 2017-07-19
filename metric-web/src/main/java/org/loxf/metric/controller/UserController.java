package org.loxf.metric.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import org.loxf.metric.api.IUserService;
import org.loxf.metric.base.annotations.Permission;
import org.loxf.metric.base.constants.PermissionType;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.QuotaDto;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.dal.po.User;
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
    @Autowired
    private IUserService userService;

    /**
     * 主用户注册
     *
     * @return
     */

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    @ResponseBody
    @ApiOperation(value = "主用户注册", notes = "主用户注册", httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<String> registerRoot(@ApiParam(value = "用户手机号", name = "phone", required = true) String phone,
                                           @ApiParam(value = "用户密码", name = "pwd", required = true) String pwd,
                                           @ApiParam(value = "真实姓名", name = "realName", required = true) String realName,
                                           @ApiParam(value = "真实姓名", name = "verifyCode", required = true) String verifyCode) {
        return userService.register(phone, pwd, realName);
    }

    /**
     * 登录
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "登录", notes = "主用户登录只需要手机和密码，子用户需要团队码",
            httpMethod = "POST", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<String> login(@ApiParam(value = "用户手机号", name = "phone", required = true) String phone,
                                    @ApiParam(value = "密码/验证码", name = "pwd", required = true) String pwd,
                                    @ApiParam(value = "团队码", name = "uniqueCode") String uniqueCode,
                                    @ApiParam(value = "用户类型：ROOT/CHILD", name = "userType", required = true) String userType,
                                    @ApiParam(value = "登录类型：PHONE/PC", name = "loginType", required = true) String loginType,
                                    HttpServletRequest request, HttpServletResponse response) {
        BaseResult<UserDto> userDtoBaseResult = null;
        if ("PC".equals(loginType)) {
            userDtoBaseResult = userService.login(phone, pwd, uniqueCode, userType);
            if (!ResultCodeEnum.SUCCESS.getCode().equals(userDtoBaseResult.getCode())) {
                return new BaseResult<>(userDtoBaseResult.getCode(), userDtoBaseResult.getMsg());
            }
        } else {
            // TODO 校验验证码
            if (pwd.equals("L123")) {
                userDtoBaseResult = userService.queryUser(phone, uniqueCode, userType);
                if (!ResultCodeEnum.SUCCESS.getCode().equals(userDtoBaseResult.getCode())) {
                    return new BaseResult<>(userDtoBaseResult.getCode(), userDtoBaseResult.getMsg());
                }
            }
        }
        UserDto userDtoData = userDtoBaseResult.getData();
        userDtoData.setUserAgent(request.getHeader("User-Agent"));
        String sessionId = IdGenerator.generate("SES", 64);// sessionID不能用用户名，因为用户名是固定的，可能被别人获取盗用。
        LoginFilter.setUser(request, sessionId, userDtoData);//添加用户信息到session中
        LoginFilter.addSessionId(response, sessionId);// 添加cookie信息
        return new BaseResult<>();
    }

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
        return userService.addChildUser(userDto);
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
    @ApiOperation(value = "修改密码", notes = "修改密码",
            httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<String> modifyPwd(@RequestParam String oldPwd, @RequestParam String newPwd, HttpServletRequest request, HttpServletResponse response) {
        UserDto existsUser = LoginFilter.getUser(request);
        return userService.modifyPwd(existsUser, oldPwd, newPwd);
    }

}
