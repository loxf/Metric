package org.loxf.metric.controller;

import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.loxf.metric.api.IUserService;
import org.loxf.metric.base.constants.RateLimitType;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.filter.LoginFilter;
import org.loxf.metric.requestInfo.SmsRequestInfo;
import org.loxf.metric.utils.SendMsgUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by luohj on 2017/7/22.
 */
@Controller
@RequestMapping("/authen")
@Api(value = "login", description = "登录、注册、修改密码")
public class LoginControl {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private IUserService userService;

    /**
     * 获取短信验证码
     *
     * @return
     */

    @RequestMapping(value = "/getSMSValidateCode", method = RequestMethod.GET, consumes = "application/json;charset=UTF-8")
    @ResponseBody
    @ApiOperation(value = "获取验证码", notes = "获取验证码", httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<String> getSMSValidateCode(@ApiParam(value = "用户手机号", name = "phone", required = true) String phone,
                                                 @ApiParam(value = "验证码类型(仅限登录和注册)", name = "validateType", required = true) String validateType,
                                                 HttpServletRequest request) {
        SmsRequestInfo smsRequestInfo=new SmsRequestInfo(phone,null,validateType,false);
        if(!smsRequestInfo.validate()){
            return new BaseResult<>(ResultCodeEnum.PARAM_ERROR.getCode(), smsRequestInfo.getErrorMsg());

        }
        String code=SendMsgUtils.sendMsgByType(validateType,phone);
        if(code==null){
            return new BaseResult<>(ResultCodeEnum.PARAM_ERROR.getCode(), "验证码发送失败!");
        }
        HttpSession session=request.getSession();
        session.setAttribute(phone+validateType,code);
        session.setMaxInactiveInterval(60);
        return new BaseResult<>();
    }

    /**
     * 校验验证码
     * @param phone
     * @param validateCode
     * @param validateType
     * @param request
     * @return
     */
    private boolean validateSmsCode(String phone,String validateCode,String validateType,StringBuilder error,HttpServletRequest request) {
        SmsRequestInfo smsRequestInfo=new SmsRequestInfo(phone,validateCode,validateType,true);
        if(!smsRequestInfo.validate()){
            error.append(smsRequestInfo.getErrorMsg());
            return false;
        }
        HttpSession session=request.getSession();
        if(session.getAttribute(phone+validateType)==null){
            error.append("验证码已过期或未获取验证码，请重新获取验证码!");
            return false;
        }
        if(!validateCode.equals((String)session.getAttribute(phone+validateType))){
            error.append("验证码错误!");
            return false;
        }
        return true;
    }


    /**
     * 主用户注册
     *
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET, produces = "application/json;charset=UTF-8",consumes = "application/json;charset=UTF-8")
    @ResponseBody
    @ApiOperation(value = "主用户注册", notes = "主用户注册", httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<String> registerRoot(@ApiParam(value = "用户手机号", name = "phone", required = true) String phone,
                                           @ApiParam(value = "用户密码", name = "pwd", required = true) String pwd,
                                           @ApiParam(value = "真实姓名", name = "realName", required = true) String realName,
                                           @ApiParam(value = "验证码", name = "verifyCode", required = true) String verifyCode,
                                           HttpServletRequest request) {//realName乱码，配置tomcat
        StringBuilder error=new StringBuilder();
        if(!validateSmsCode(phone,verifyCode,RateLimitType.REGISTERCODE,error,request)){
            return new BaseResult<>(ResultCodeEnum.PARAM_ERROR.getCode(), error.toString());
        }
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
    public BaseResult<String> login(@RequestParam("phone") @ApiParam(value = "用户手机号", name = "phone", required = true) String phone,
                                    @RequestParam("pwd") @ApiParam(value = "密码/验证码", name = "pwd", required = true) String pwd,
                                    @RequestParam(value = "uniqueCode", required = false) @ApiParam(value = "团队码", name = "uniqueCode") String uniqueCode,
                                    @RequestParam("userType") @ApiParam(value = "用户类型：ROOT/CHILD", name = "userType", required = true) String userType,
                                    @RequestParam("loginType") @ApiParam(value = "登录类型：PHONE/PC", name = "loginType", required = true) String loginType,
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
        String sessionId = IdGenerator.generate("SES", 32);// sessionID不能用用户名，因为用户名是固定的，可能被别人获取盗用。
        LoginFilter.setUser(request, sessionId, userDtoData);//添加用户信息到session中
        LoginFilter.addSessionId(response, sessionId);// 添加cookie信息
        return new BaseResult<>(userDtoData.getRealName());
    }

    /**
     * 修改密码
     *
     * @param phone
     * @param verifyCode
     * @param uniqueCode
     * @param newPwd
     * @return
     */
    @RequestMapping(value = "/modifyPwd", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "修改密码", notes = "修改密码，通过老密码修改",
            httpMethod = "GET", response = BaseResult.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编码见枚举值", response = ResultCodeEnum.class)})
    public BaseResult<String> modifyPwd(@RequestParam String phone, @RequestParam String verifyCode,
                                        @RequestParam(required = false) String uniqueCode, @RequestParam String newPwd, HttpServletRequest request, HttpServletResponse response) {
        // TODO 验证码校验
        if("L123".equals(verifyCode)){
            return userService.modifyPwd(phone, uniqueCode, newPwd);
        } else {
            return new BaseResult<>(ResultCodeEnum.ILLEGAL_REQUEST.getCode(), "验证码校验失败");
        }
    }
}
