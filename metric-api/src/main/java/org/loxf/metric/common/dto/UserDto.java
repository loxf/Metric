package org.loxf.metric.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by hutingting on 2017/7/6.
 */
@ApiModel("用户")
public class UserDto extends BaseDto{
    @ApiModelProperty("用户名，自动编码")
    private String userName;
    @ApiModelProperty("团队码")
    private String uniqueCode;
    @ApiModelProperty("密码")
    private String pwd;
    @ApiModelProperty("真实姓名")
    private String realName;
    @ApiModelProperty("手机号码")
    private String phone;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("用户类型:CHILD/ROOT")
    private String userType;
    @ApiModelProperty("状态:AVAILABLE(生效)/DISABLED(失效)")
    private String state;
    /**
     * 浏览器信息
     */
    private String userAgent;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}

