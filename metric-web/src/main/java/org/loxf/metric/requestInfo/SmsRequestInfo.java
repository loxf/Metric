package org.loxf.metric.requestInfo;

import org.apache.commons.lang.StringUtils;
import org.loxf.metric.base.constants.RateLimitType;
import org.loxf.metric.base.utils.ValideDataUtils;
import org.loxf.metric.common.constants.ResultCodeEnum;
import org.loxf.metric.common.dto.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hutingting on 2017/7/26.
 */
public class SmsRequestInfo implements Serializable {

    private String phone;

    private String validateCode;

    private String smsType;

    private boolean isValidate;

    private List<String> errorList;

    public boolean validate() {
        errorList = new ArrayList();
        validateBase();
        return errorList.isEmpty();
    }

    private void validateBase() {
        if (isValidate) {
            if (StringUtils.isBlank(phone) || StringUtils.isBlank(validateCode) || StringUtils.isBlank(smsType)) {
                errorList.add("手机号、验证码、验证码类型不能为空!");
                return;
            }
        }else{
            if (StringUtils.isBlank(phone) || StringUtils.isBlank(smsType)) {
                errorList.add("手机号、验证码类型不能为空!");
                return;
            }
        }

        if (!(RateLimitType.LOGINCODE.equals(smsType) || RateLimitType.REGISTERCODE.equals(smsType))) {//只能发送登录或者注册验证码
            errorList.add("验证码类型错误!");
        }
        if (!ValideDataUtils.mobileValidate(phone)) {
            errorList.add("手机号格式错误!");
        }
    }

    public String getErrorMsg() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String error : errorList) {
            stringBuilder.append(error);
        }
        return stringBuilder.toString();
    }

    public SmsRequestInfo(String phone, String validateCode, String smsType, boolean isValidate) {
        this.phone = phone;
        this.validateCode = validateCode;
        this.smsType = smsType;
        this.isValidate = isValidate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }
}
