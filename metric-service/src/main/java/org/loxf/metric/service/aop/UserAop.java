package org.loxf.metric.service.aop;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.common.constants.PermissionType;
import org.loxf.metric.common.constants.UserTypeEnum;
import org.loxf.metric.dal.dao.interfaces.UserDao;
import org.loxf.metric.dal.po.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by luohj on 2017/7/13.
 */
@Component
@Aspect
public class UserAop {
    private static Logger logger = LoggerFactory.getLogger(UserAop.class);
    @Autowired
    private UserDao userDao;

    @Before("@annotation(checkUser)")
    public void checkUserRole(JoinPoint point, CheckUser checkUser){
        // 获取权限类型
        PermissionType permissionType = checkUser.value();
        if(permissionType.equals(PermissionType.NONE)) {
            // 无需校验权限
            return;
        } else {
            // 需校验权限
            String nameParam = checkUser.nameParam();// 获取权限所需的经办人的取值入参对应的位置及其他配置 形式：{0}.name
            String[] nameParamArr = nameParam.split("\\.");// 解析
            String location = nameParamArr[0].substring(1, nameParamArr[0].length()-1);// 先获取参数位置（索引）
            Object args [] = point.getArgs();//获取当前方法的所有入参参数
            Object obj = args[Integer.parseInt(location)];// 获取经办人所在的参数
            String userName = "";
            if(nameParamArr.length==1){
                userName = obj.toString();// 如果长度为1，代表当前入参非对象，toString获取的值就是所需参数
            } else {
                userName = getUserName(obj, nameParamArr, 1);// 当前入参为对象，递归获取
            }
            // 权限校验
            if (StringUtils.isEmpty(userName)) {
                throw new MetricException("登录用户为空!");
            } else {//判断该用户是否存在
                User user = new User();
                user.setUserName(userName);
                User existsUser = userDao.findOne(user);
                if (existsUser == null) {
                    throw new MetricException("登录用户错误!");
                } else {
                    if (permissionType.equals(PermissionType.ROOT)) {
                        //判断该用户是否为root用户
                        String type = existsUser.getUserType();
                        if (!(UserTypeEnum.ROOT.name().equals(type))) {
                            throw new MetricException("登录用户无权限!");
                        }
                    }
                }
            }
        }
    }

    private String getUserName(Object obj, String[] nameParamArr, int deep){
        Object object = null;
        if(obj instanceof Map){
            object = ((Map) obj).get(nameParamArr[deep]);
        } else {
            try {
                Method method = obj.getClass().getMethod("get"+ captureName(nameParamArr[deep]));
                object = method.invoke(obj);
            } catch (NoSuchMethodException e) {
                logger.error("权限检查失败，检查程序配置CheckUser", e);
                throw new MetricException("权限检查失败，检查程序配置CheckUser");
            } catch (IllegalAccessException e) {
                logger.error("权限检查失败，检查程序配置CheckUser", e);
                throw new MetricException("权限检查失败，检查程序配置CheckUser");
            } catch (InvocationTargetException e) {
                logger.error("权限检查失败，检查程序配置CheckUser", e);
                throw new MetricException("权限检查失败，检查程序配置CheckUser");
            }
        }
        if(deep==nameParamArr.length-1){
            return object.toString();
        }
        return getUserName(object, nameParamArr, ++deep);
    }

    public String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }
}
