package org.loxf.metric.interceptor;

import org.loxf.metric.base.annotations.Permission;
import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.base.constants.PermissionType;
import org.loxf.metric.common.constants.UserTypeEnum;
import org.loxf.metric.common.dto.UserDto;
import org.loxf.metric.filter.LoginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by luohj on 2017/7/13.
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(PermissionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Class<?> clazz = handler.getClass();
        if(handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            if (clazz.isAnnotationPresent(Permission.class)) {
                Permission permission = ((HandlerMethod) handler).getMethodAnnotation(Permission.class);
                if(permission==null){
                    return true;
                }
                return checkUserRole(request, permission);
            }
        }
        return true;
    }

    public boolean checkUserRole(HttpServletRequest request, Permission permission) {
        // 获取权限类型
        PermissionType permissionType = permission.value();
        // 权限校验
        UserDto existsUser = LoginFilter.getUser(request);
        if (existsUser == null) {
            throw new MetricException("用户未登录!");
        } else {
            if (permissionType.equals(PermissionType.ROOT)) {
                //判断该用户是否为root用户
                String type = existsUser.getUserType();
                if (!(UserTypeEnum.ROOT.name().equals(type))) {
                    throw new MetricException("登录用户无权限!");
                }
            } else if (permissionType.equals(PermissionType.SPECIAL)) {
                // 指定权限校验
                String permissionCode = permission.permissionCode();// 所需权限
                // 以下代码未实现，以后用户实现权限控制后，可放开
                        /*List<Permission> permissionList = existsUser.getPermissionList();
                        if(!permissionList.contains(new Permission(permissionCode))){
                            throw new MetricException("登录用户无权限!");
                        }*/
            }
        }
        return true;
    }
}
