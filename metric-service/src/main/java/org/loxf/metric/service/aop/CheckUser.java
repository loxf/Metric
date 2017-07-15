package org.loxf.metric.service.aop;

import org.loxf.metric.common.constants.PermissionType;

import java.lang.annotation.*;

/**
 * Created by luohj on 2017/7/13.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckUser {
    PermissionType value() default PermissionType.LOGIN;

    /**
     * 指定获取用户名的参数的位置，如果参数是对象或者map，先指定位置，再指定节点名称
     * {0}:String类型
     * {0}.userName:一级
     * {0}.target.userName:多级
     * @return
     */
    String nameParam() default "{0}";

    /**
     * 权限编码，配合PermissionType.SPECIAL使用
     * @return
     */
    String permissionCode() default "";
}
