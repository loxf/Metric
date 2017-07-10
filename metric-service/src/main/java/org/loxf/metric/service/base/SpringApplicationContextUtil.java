package org.loxf.metric.service.base;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by caiyang on 2017/3/28.
 */
public class SpringApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        SpringApplicationContextUtil.applicationContext = applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static Object getBean(Class<? extends Object> clazz) {
        return applicationContext.getBean(clazz);
    }

}
