package org.loxf.metric.core.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by luohj on 2017/5/5.
 */
public class DataSourceAspect {
    private static Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    private String defaultDataBase;
    /**
     * 拦截目标方法，获取由@DataSource指定的数据源标识，设置到线程存储中以便切换数据源
     *
     * @param point
     * @throws Exception
     */
    public void intercept(JoinPoint point){
        Class<?> target = point.getTarget().getClass();
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 默认使用目标类型的注解，如果没有则使用其实现接口的注解
        boolean flag = false;
        for (Class<?> clazz : target.getInterfaces()) {
            flag = resolveDataSource(clazz, signature.getMethod(), flag);
        }
        flag = resolveDataSource(target, signature.getMethod(), flag);
        if(!flag){
            //未设置数据 使用默认设置
            DynamicDataSourceHolder.setDataSource(defaultDataBase);
        }
    }

    /**
     * 提取目标对象方法注解和类型注解中的数据源标识
     *
     * @param clazz
     * @param method
     */
    private boolean resolveDataSource(Class<?> clazz, Method method, boolean flag) {
        try {
            // 默认使用类型注解
            if (clazz.isAnnotationPresent(DataSource.class)) {
                DataSource source = clazz.getAnnotation(DataSource.class);
                DynamicDataSourceHolder.setDataSource(source.value());
                logger.debug("使用类定义的数据库：" + source.value());
                flag = true;
            }
            // 方法注解可以覆盖类型注解
            Class<?>[] types = method.getParameterTypes();
            Method m = clazz.getMethod(method.getName(), types);
            if (m != null && m.isAnnotationPresent(DataSource.class)) {
                DataSource source = m.getAnnotation(DataSource.class);
                DynamicDataSourceHolder.setDataSource(source.value());
                logger.debug("使用方法定义的数据库：" + source.value());
                flag = true;
            }
        } catch (Exception e) {
            logger.error(clazz + ":" + e.getMessage(), e);
        }
        return flag;
    }

    public String getDefaultDataBase() {
        return defaultDataBase;
    }

    public void setDefaultDataBase(String defaultDataBase) {
        this.defaultDataBase = defaultDataBase;
    }
}
