package org.loxf.metric.service.impl;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.loxf.metric.api.IUserService;
import org.loxf.metric.base.annotations.Permission;
import org.loxf.metric.base.utils.IdGenerator;
import org.loxf.metric.base.utils.MapAndBeanTransUtils;
import org.loxf.metric.base.utils.ValideDataUtils;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.base.constants.StandardState;
import org.loxf.metric.common.constants.UserTypeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.Pager;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.dal.dao.interfaces.UserDao;
import org.loxf.metric.dal.po.User;
import org.loxf.metric.service.base.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by hutingting on 2017/7/14.
 */
@Service("userService")
public class UserServiceImpl extends BaseService implements IUserService {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private UserDao userDao;
    //ctrl+shif+f：格式化
    //ctrl+b：全项目编译
    @Override
    public BaseResult<UserDto> login(String phone, String pwd, String teamCode, String type) {//手机号码登录
        BaseResult<UserDto> result = new BaseResult<>();
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(pwd) || StringUtils.isBlank(type)) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        String userType = null;
        if (UserTypeEnum.CHILD.name().equals(type)) {
            if (StringUtils.isBlank(teamCode)) {
                result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
                result.setMsg("子用户登录必须填写团队码!");
                return result;
            } else {
                userType = UserTypeEnum.CHILD.name();
            }
        } else if (UserTypeEnum.ROOT.name().equals(type)) {
            userType = UserTypeEnum.ROOT.name();
        }
        User qryUser = new User();
        qryUser.setUniqueCode(teamCode);
        qryUser.setUserType(userType);
        qryUser.setPhone(phone);
        qryUser.setPwd(pwd);
        qryUser.setState(StandardState.AVAILABLE.getValue());
        User existUser = userDao.findOne(qryUser);
        if (existUser == null) {
            result.setCode(ResultCodeEnum.DATA_NOT_EXIST.getCode());
            result.setMsg("用户不存在");
            return result;
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(existUser, userDto);
        result.setData(userDto);
        return result;
    }

    /**
     * 注册
     *
     * @param phone
     * @param pwd
     * @return
     */
    @Override
    public BaseResult<String> register(String phone, String pwd, String realName) {//主用户注册，自动生成userName，团队码
        BaseResult<String> result = new BaseResult<>();
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(pwd) || StringUtils.isBlank(realName)) {//校验参数是否为空
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        if (!ValideDataUtils.mobileValidate(phone)) {//校验手机号格式
            result.setCode(ResultCodeEnum.PARAM_ERROR.getCode());
            result.setMsg("手机号格式错误!");
            return result;
        }
        User qryUser = new User();
        qryUser.setUserType(UserTypeEnum.ROOT.name());
        qryUser.setPhone(phone);
        User existUser = userDao.findOne(qryUser);
        if (existUser != null) {//校验是否已存在该手机号的主用户
            result.setCode(ResultCodeEnum.DATA_EXIST.getCode());
            result.setMsg("该手机号已注册!");
            return result;
        }
        qryUser.setState(StandardState.AVAILABLE.getValue());
        //自动生成userName和团队码
        String generateTeamCode = IdGenerator.generate(null, 8);
        qryUser.setUniqueCode(generateTeamCode);
        qryUser.setPwd(pwd);
        qryUser.setRealName(realName);
        userDao.insert(qryUser);
        return result;
    }

    @Override
    public BaseResult<String> addChildUser(UserDto obj) {//校验email
        BaseResult<String> result = new BaseResult<String>();
        if (StringUtils.isEmpty(obj.getUniqueCode()) ||StringUtils.isEmpty(obj.getEmail()) || StringUtils.isEmpty(obj.getRealName()) || StringUtils.isEmpty(obj.getPhone())) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg("手机号、真实姓名、email、团队码必填!");
            return result;
        }
        if (!ValideDataUtils.mobileValidate(obj.getPhone())) {
            result.setCode(ResultCodeEnum.PARAM_ERROR.getCode());
            result.setMsg("手机号校验失败!");
            return result;
        }
        //校验userName是否有重复
        User qryUser = new User();
        qryUser.setPhone(obj.getPhone());
        qryUser.setUserType(UserTypeEnum.CHILD.name());
        qryUser.setUniqueCode(obj.getUniqueCode());
        qryUser.setState(StandardState.AVAILABLE.getValue());
        User existUser = userDao.findOne(qryUser);
        if (existUser != null) {
            result.setCode(ResultCodeEnum.DATA_EXIST.getCode());
            result.setMsg("该子用户已存在!");
            return result;
        }
        //子用户数量暂时不作限制
        qryUser.setCreateUserName(obj.getHandleUserName());
        qryUser.setRealName(obj.getRealName());
        qryUser.setEmail(obj.getEmail());
        String generatePwd=IdGenerator.generate(null,6);
        qryUser.setPwd(generatePwd);
        userDao.insert(qryUser);
        //建立子用户时自动生成密码发送到手机,userName和密码自动生成。主用户密码自己设置
        return result;
    }

    /**
     * 注销子用户
     * @param userName
     * @return
     */
    @Override
    public BaseResult<String> disableChildUser(String userName,String rootName,String teamCode){
        BaseResult<String> result=new BaseResult<String>();
        if (StringUtils.isEmpty(userName)||StringUtils.isEmpty(rootName)||StringUtils.isEmpty(teamCode)) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg("必填参数缺失!");
            return result;
        }
        User qryUser=new User();
        qryUser.setUserName(userName);
        qryUser.setUniqueCode(teamCode);
        qryUser.setUserType(UserTypeEnum.CHILD.name());
        User existUser = userDao.findOne(qryUser);
        if(existUser==null){
            result.setCode(ResultCodeEnum.DATA_NOT_EXIST.getCode());
            result.setMsg("不存在该用户!");
            return result;
        }
        Map setMap=new HashedMap();
        setMap.put("state",StandardState.DISABLED.getValue());
        setMap.put("updateUserName",rootName);
        userDao.updateOne(userName,setMap);
        return result;
    }

    /**
     * 修改密码
     * @param userDto
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @Override
    @Permission
    public  BaseResult<String> modifyPwd(UserDto userDto, String oldPwd,String newPwd){
        BaseResult<String> result=new BaseResult<>();
        if(userDto==null||StringUtils.isBlank(oldPwd)||StringUtils.isBlank(newPwd)){
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg("必填参数缺失!");
            return result;
        }
        if(!userDto.getPwd().equals(oldPwd)){
            result.setCode(ResultCodeEnum.DATA_NOT_EXIST.getCode());
            result.setMsg("用户名和旧密码不匹配!");
            return result;
        }
        User user=new User();
        BeanUtils.copyProperties(userDto,user);
        user.setPwd(newPwd);
        Map setMap=MapAndBeanTransUtils.transBean2Map(user);
        userDao.updateOne(user.getUserName(),setMap);
        return result;
    }

    /**
     * 修改密码（通过手机号码）
     * @param phone
     * @param newPwd
     * @return
     */
    public  BaseResult<String> modifyPwd(String phone, String uniqueCode, String newPwd){
        if(StringUtils.isBlank(phone)||StringUtils.isBlank(uniqueCode)||StringUtils.isBlank(newPwd)){
            return new BaseResult<>(ResultCodeEnum.PARAM_LACK.getCode(), "必填参数缺失!");
        }
        User user=new User();
        user.setUniqueCode(uniqueCode);
        user.setPhone(phone);
        User existsUser = userDao.findOne(user);
        if(existsUser==null){
            return new BaseResult<>(ResultCodeEnum.USER_NOT_EXIST.getCode(), ResultCodeEnum.USER_NOT_EXIST.getCodeMsg());
        }
        User setParam = new User();
        setParam.setPwd(newPwd);
        Map setMap=MapAndBeanTransUtils.transBean2Map(setParam);
        userDao.updateOne(user.getUserName(), setMap);
        return new BaseResult<>();
    }

    /**
     * 更换手机号码
     * @param obj
     * @return
     */
    @Override
    public BaseResult<String> insertItem(UserDto obj) {
        return null;
    }

    @Override
    public BaseResult<PageData<UserDto>> getPageList(UserDto obj) {
        BaseResult result = new BaseResult();
        if (StringUtils.isEmpty(obj.getUniqueCode())) {//只能查询所属团队的成员
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        Pager pager = obj.getPager();
        result = super.validPager(obj.getPager());
        if (ResultCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
            obj.setState(StandardState.AVAILABLE.getValue());
            Map<String, Object> params = MapAndBeanTransUtils.transBean2Map(obj);
            result.setData(getPageResult(userDao, params, pager.getStart(), pager.getRownum()));
        }
        return result;
    }


    @Override
    public BaseResult<UserDto> queryItemByCode(String itemCode, String handleUserName) {
        BaseResult<UserDto> result = new BaseResult<>();
        if (StringUtils.isEmpty(itemCode)) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        User user = new User();
        user.setUserName(itemCode);
        User userData = userDao.findOne(user);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userData, userDto);//前者赋值给后者
        result.setData(userDto);
        return result;
    }

    @Override
    public BaseResult<String> updateItem(UserDto obj) {
        BaseResult<String> result = new BaseResult<String>();
        String itemCode = obj.getUserName();
        if (StringUtils.isEmpty(itemCode)) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        User paramUser = new User();
        BeanUtils.copyProperties(obj, paramUser);
        paramUser.setUpdateUserName(obj.getHandleUserName());
        Map<String, Object> boardMap = MapAndBeanTransUtils.transBean2Map(paramUser);
        StringBuilder updateParams = new StringBuilder();
        for (Map.Entry<String, Object> entry : boardMap.entrySet()) {
            updateParams.append(entry.getKey() + ":" + entry.getValue() + ";");
        }
        logger.info(obj.getHandleUserName() + "将更新userCode=" + itemCode + "的数据为" + updateParams);
        userDao.updateOne(itemCode, boardMap);
        return result;
    }

    @Override
    public BaseResult<String> delItemByCode(String itemCode, String handleUserName) {
        logger.info("客户:" + handleUserName + "将要删除子用户,userCode" + itemCode);
        BaseResult<String> result = new BaseResult<>();
        if (StringUtils.isEmpty(itemCode)) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg("userCode不能为空!");
            return result;
        }
        Map chartMap = new HashedMap();
        chartMap.put("state", StandardState.DISABLED.getValue());
        chartMap.put("updateUserName", handleUserName);
        userDao.updateOne(itemCode, chartMap);
        return result;
    }

    @Override
    public BaseResult<UserDto> queryUser(String phone, String teamCode, String userType) {
        BaseResult<UserDto> result = new BaseResult<>();
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(userType)) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        if (UserTypeEnum.CHILD.name().equals(userType)) {
            if (StringUtils.isBlank(teamCode)) {
                result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
                result.setMsg("获取子用户必须填写团队码!");
                return result;
            }
        }
        User qryUser = new User();
        qryUser.setUniqueCode(teamCode);
        qryUser.setUserType(userType);
        qryUser.setPhone(phone);
        qryUser.setState(StandardState.AVAILABLE.getValue());
        User existUser = userDao.findOne(qryUser);
        if (existUser == null) {
            result.setCode(ResultCodeEnum.DATA_NOT_EXIST.getCode());
            result.setMsg("用户不存在");
            return result;
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(existUser, userDto);
        result.setData(userDto);
        return result;
    }

}
