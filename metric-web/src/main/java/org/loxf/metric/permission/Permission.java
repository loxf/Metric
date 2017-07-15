package org.loxf.metric.permission;

import org.loxf.metric.common.constants.PermissionType;

import java.lang.annotation.*;

/**
 * Created by luohj on 2017/7/13.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    PermissionType value() default PermissionType.LOGIN;

    /**
     * 权限编码，配合PermissionType.SPECIAL使用
     * @return
     */
    String permissionCode() default "";
}
