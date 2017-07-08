package org.loxf.metric.biz.utils;

import java.io.Serializable;
import java.util.Properties;

/**
 * 加载属性文件的值
 * Created by caiyang on 2017/4/18.
 */
public class ConfigUtil implements Serializable {
    private Properties properties;
    public Properties getProperties() {
        return properties;
    }
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
