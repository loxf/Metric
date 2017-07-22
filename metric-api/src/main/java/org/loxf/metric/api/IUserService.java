package org.loxf.metric.api;


import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.UserDto;

/**
 * Created by hutingting on 2017/7/14.
 */
public interface IUserService extends IBaseService<UserDto> {
    public BaseResult<UserDto> login(String phone, String pwd, String teamCode, String type);

    public BaseResult<String> register(String phone, String pwd, String realName);

    public BaseResult<String> addChildUser(UserDto obj);

    public BaseResult<String> disableChildUser(String userName,String rootName,String teamCode);

    public  BaseResult<String> modifyPwd(UserDto userDto,String oldPwd,String newPwd);

    public  BaseResult<String> modifyPwd(String phone, String uniqueCode, String newPwd);

    public BaseResult<UserDto> queryUser(String phone, String teamCode, String userType);
}
