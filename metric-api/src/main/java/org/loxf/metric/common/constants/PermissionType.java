package org.loxf.metric.common.constants;

/**
 * Created by hutingting on 2017/7/11.
 */
public enum PermissionType {
    ROOT("ROOT", "主用户权限"),
    CHILD("CHILD", "子用户权限"),
    LOGIN("LOGIN", "登录权限"),
    SPECIAL("SPECIAL", "指定权限"),
    NONE("NONE", "无权限");

    PermissionType(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
