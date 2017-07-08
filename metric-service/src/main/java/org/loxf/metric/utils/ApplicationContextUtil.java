package org.loxf.metric.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by hutingting on 2017/7/7.
 */
public class ApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static Object getBean(Class<? extends Object> clazz) {
        return applicationContext.getBean(clazz);
    }

}
