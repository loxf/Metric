package org.loxf.metric.biz.utils;


import org.loxf.metric.biz.base.SpringApplicationContextUtil;

import java.io.Serializable;

/**
 * 获取属性文件的值
 * Created by caiyang on 2017/4/18.
 */
public class PropertiesUtil implements Serializable {
    /**
     * @param key
     * @return
     */
    public static String getValue(String key) {
        ConfigUtil configUtil = (ConfigUtil) SpringApplicationContextUtil.getBean(ConfigUtil.class);
        return   configUtil.getProperties().getProperty(key);
    }
}
