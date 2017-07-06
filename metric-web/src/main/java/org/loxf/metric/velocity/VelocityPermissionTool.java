package org.loxf.metric.velocity;

import org.loxf.metric.filter.LoginFilter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wugang on 2017/3/18.
 */
public class VelocityPermissionTool {
/*
    public boolean hasPermission(String permissionId) {
        return LoginFilter.hasPermission(getRequest(),permissionId);
    }
*/

    public boolean equalPermission(String permissionId1,String permissionId2){
        return permissionId1.equalsIgnoreCase(permissionId2);
    }

    public HttpServletRequest getRequest(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return  request;
    }
}
