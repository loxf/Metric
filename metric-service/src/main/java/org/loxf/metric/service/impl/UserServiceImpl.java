package org.loxf.metric.service.impl;

import org.apache.log4j.Logger;
import org.loxf.metric.api.IUserService;
import org.loxf.metric.base.utils.MapAndBeanTransUtils;
import org.loxf.metric.base.utils.ValideDataUtils;
import org.loxf.metric.common.constants.PermissionType;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.constants.StandardState;
import org.loxf.metric.common.constants.UserTypeEnum;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.Pager;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.dal.dao.interfaces.UserDao;
import org.loxf.metric.dal.po.User;
import org.loxf.metric.service.aop.CheckUser;
import org.loxf.metric.service.base.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/14.
 */
@Service("userService")
public class UserServiceImpl extends BaseService implements IUserService {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private UserDao userDao;

    @Override
    @CheckUser(nameParam = "{0}.handleUserName")
    public BaseResult<PageData> getPageList(UserDto obj) {
        BaseResult<PageData> result=new BaseResult<>();
        if(StringUtils.isEmpty(obj.getUniqueCode())){//只能查询所属团队的成员
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        Pager pager=obj.getPager();
        String validPagerResult=super.validPager(obj.getPager());
        if(!"SUCCESS".equals(validPagerResult)){
            result.setCode(ResultCodeEnum.PARAM_ERROR.getCode());
            result.setMsg(validPagerResult);
            return result;
        }
        obj.setState(StandardState.AVAILABLE.getValue());
        Map<String, Object> params = MapAndBeanTransUtils.transBean2Map(obj);
        result.setData(getPageResult(userDao, params, pager.getStart(), pager.getRownum()));
        return result;
    }


    @Override
    @CheckUser(value = PermissionType.ROOT, nameParam = "{0}.handleUserName")
    public BaseResult<String> insertItem(UserDto obj) {
        BaseResult<String> result=new BaseResult<String>();
        if(StringUtils.isEmpty(obj.getUserName())||StringUtils.isEmpty(obj.getPwd())||
                StringUtils.isEmpty(obj.getRealName())||StringUtils.isEmpty(obj.getPhone())||
                StringUtils.isEmpty(obj.getUserName())||StringUtils.isEmpty(obj.getUniqueCode())){
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        if(!ValideDataUtils.mobileValidate(obj.getPhone())){
            result.setCode(ResultCodeEnum.PARAM_ERROR.getCode());
            result.setMsg(ResultCodeEnum.PARAM_ERROR.getCodeMsg());
            return result;
        }
        //校验userName是否有重复
        User qryUser=new User();
        qryUser.setUserName(obj.getUserName());
        User existUser=userDao.findOne(qryUser);
        if(existUser!=null){
            result.setCode(ResultCodeEnum.DATA_EXIST.getCode());
            result.setMsg(ResultCodeEnum.DATA_EXIST.getCodeMsg());
            return result;
        }
        //子用户数量暂时不作限制
        User user = new User();
        BeanUtils.copyProperties(obj, user);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setState(StandardState.AVAILABLE.getValue());
        user.setUserType(UserTypeEnum.CHILD.name());
        user.setCreateUserName(obj.getHandleUserName());
        result.setData(userDao.insert(user));
        return result;
    }

    @Override
    @CheckUser(nameParam = "{1}")
    public BaseResult<UserDto> queryItemByCode(String itemCode, String handleUserName) {
        BaseResult<UserDto> result=new BaseResult<>();
        if(StringUtils.isEmpty(itemCode)){
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return  result;
        }
        User user = new User();
        user.setUserCode(itemCode);
        User userData = userDao.findOne(user);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userData, userDto);//前者赋值给后者
        result.setData(userDto);
        return result;
    }

    @Override
    @CheckUser(nameParam = "{0}.handleUserName")
    public BaseResult<String> updateItem(UserDto obj) {
        BaseResult<String> result=new BaseResult<String>();
        String itemCode = obj.getUserCode();
        if (StringUtils.isEmpty(itemCode)) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        obj.setUpdateUserName(obj.getHandleUserName());
        obj.setUpdatedAt(new Date());
        Map userMap = MapAndBeanTransUtils.transBean2Map(obj);
        userDao.updateOne(itemCode, userMap);
        return result;
    }

    @Override
    @CheckUser(value = PermissionType.ROOT, nameParam = "{1}")
    public BaseResult<String> delItemByCode(String itemCode, String handleUserName) {
        BaseResult<String> result=new BaseResult<>();
        if (StringUtils.isEmpty(itemCode)) {
            result.setCode(ResultCodeEnum.PARAM_LACK.getCode());
            result.setMsg(ResultCodeEnum.PARAM_LACK.getCodeMsg());
            return result;
        }
        userDao.remove(itemCode);
        return result;
    }

}
