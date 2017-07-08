package org.loxf.metric.service;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.dal.dao.interfaces.UserDao;
import org.loxf.metric.dal.po.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * Created by hutingting on 2017/7/7.
 */
@Component
public class UserManager {

    @Autowired
    private UserDao userDao;

    public String insert(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        return userDao.insert(user);
    }

    public UserDto getUserDtoByParams(Map<String, Object> params) {
        UserDto userDto=new UserDto();
        User user = getUserByParams(params);
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    public User getUserByParams(Map<String, Object> params) {
        return userDao.findOne(params);
    }

    public void updateUserByCode(String userCode,Map<String, Object> setParams) {
        Map<String, Object> qryParams=new HashedMap();
        qryParams.put("userCode",userCode);
        setParams.put("updatedAt",new Date());
        userDao.update(qryParams,setParams);
    }

    public void delUserByCode(String userCode) {
        Map<String, Object> qryParams=new HashedMap();
        qryParams.put("userCode",userCode);
        userDao.remove(qryParams);
    }
}
