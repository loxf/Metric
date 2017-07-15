package org.loxf.metric.base.utils;

import org.apache.commons.lang.StringUtils;

/**
 * Created by hutingting on 2017/7/14.
 */
public class ValideDataUtils {
    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
    // 完整的判断中文汉字和符号
    private static boolean isChinese(String paramStr) {
        char[] ch = paramStr.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    //判断是否为11位数字，同时，第一位必须为1
    private static boolean mobileValidate0(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return false;
        }
        if (mobile.length() != 11 && mobile.length() != 14) {
            return false;
        }
        if (mobile.length() == 14) {
            String code = mobile.substring(0, 3);
            String realNumber = mobile.substring(3, mobile.length());
            if ("+86".equals(code)) {
                return mobileElevenValidate0(realNumber);
            } else {
                return false;
            }

        }
        return  mobileElevenValidate0(mobile);
    }

    //判断是否所有的字符相同
    private static boolean isAllTheSame(String paramStr) {
        char[] ch = paramStr.toCharArray();
        char first = ch[0];
        for (int i = 1; i < ch.length; i++) {
            char next = ch[i];
            if (first != next) {
                return false;
            }
        }
        return true;
    }
    private static boolean mobileElevenValidate0(String mobile) {
        //判断是否所有字符相同
        if (isAllTheSame(mobile)) {
            return false;
        }
        char first = mobile.charAt(0);
        if (first != '1') {
            return false;
        }

        return StringUtils.isNumeric(mobile);
    }


    /**
     * 校验手机号是否有效
     * @param mobile
     * @return
     */
    public static boolean mobileValidate(String mobile) {
        //判断是否含有中文汉字和符号
        if (isChinese(mobile)) {
            return false;
        }
        //校验必须为11位数字，同时，第一位必须为1
        return mobileValidate0(mobile);
    }
}
