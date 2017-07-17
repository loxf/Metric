package org.loxf.metric.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.loxf.metric.api.IUserService;
import org.loxf.metric.base.annotations.Permission;
import org.loxf.metric.base.constants.PermissionType;
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
public class UserController {
    @Autowired
    private IUserService userService;

    /**
     * 主用户注册
     * @return
     */

    @RequestMapping(value="/register",method = RequestMethod.POST,consumes = "application/json;charset=UTF-8")
    @ResponseBody
    public BaseResult<String> registerRoot(@RequestBody UserDto userDto){
//        if(userDto==null){
//            return  new BaseResult<>(ResultCodeEnum.DATA_NOT_EXIST.getCode(),"没有用户信息,无法注册!");
//        }
        return userService.register(userDto.getPhone(),userDto.getPwd(),userDto.getRealName());
    }

    /**
     * 登录
     * @param userDto
     * @param request
     * @return
     */
    @RequestMapping(value="/login",method = RequestMethod.POST)//login(String phone, String pwd, String teamCode, String type)
    @ResponseBody
    public BaseResult<String> login(@RequestBody UserDto userDto,HttpServletRequest request){//登录成功放入缓存
//        if(userDto==null){
//            return  new BaseResult<>(ResultCodeEnum.DATA_NOT_EXIST.getCode(),"没有用户信息,无法登录!");
//        }
        BaseResult<UserDto> userDtoBaseResult=userService.login(userDto.getPhone(),userDto.getPwd(),userDto.getUniqueCode(),userDto.getUserType());
        if(!ResultCodeEnum.SUCCESS.getCode().equals(userDtoBaseResult.getCode())){
            return  new BaseResult<>(userDtoBaseResult.getCode(),userDtoBaseResult.getMsg());
        }
        UserDto userDtoData=userDtoBaseResult.getData();
        userDtoData.setUserAgent(userDto.getUserAgent());
        String sessionId=userDtoData.getUserName();
        LoginFilter.setUser(request,sessionId,userDtoData);//添加用户信息到session中
        return null;
    }

    /**
     * 添加子用户
     * @param userDto
     * @param request
     * @param response
     * @return
     */
    @Permission(PermissionType.ROOT)
    @RequestMapping(value="/addChildUser",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> addChildUser(@RequestBody UserDto userDto,HttpServletRequest request,HttpServletResponse response){
//        if(userDto==null){
//            return  new BaseResult<>(ResultCodeEnum.DATA_NOT_EXIST.getCode(),"子用户信息为空，无法添加!");
//        }
        return userService.addChildUser(userDto);
    }

    /**
     * 注销子用户
     * @param childUserName
     * @param request
     * @param response
     * @return
     */
    @Permission(PermissionType.ROOT)
    @RequestMapping(value="/disableChildUser",method = RequestMethod.GET)
    @ResponseBody
    public BaseResult<String> disableChildUser(@RequestParam String childUserName, HttpServletRequest request, HttpServletResponse response){
        UserDto rootUser = LoginFilter.getUser(request);
        return userService.disableChildUser(childUserName,rootUser.getUserName(),rootUser.getUniqueCode());
    }

    /**
     * 修改密码
     * @param oldPwd
     * @param newPwd
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/modifyPwd",method = RequestMethod.GET)
    @ResponseBody
    public BaseResult<String> modifyPwd(@RequestParam String oldPwd,@RequestParam String newPwd,HttpServletRequest request,HttpServletResponse response){
        UserDto existsUser = LoginFilter.getUser(request);
        return userService.modifyPwd(existsUser,oldPwd,newPwd);
    }

}
