package org.loxf.metric.utils;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.api.IRateLimitService;
import org.loxf.metric.common.dto.RateLimitDto;
import org.loxf.metric.controller.RateLimitMapBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hutingting on 2017/7/22.
 */
public class RateLimitInitListener implements BeanPostProcessor {
    private Logger logger = LoggerFactory.getLogger(RateLimitInitListener.class);


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {//在spring中定义的bean初始化后调用这个方法
        try {
            if(bean instanceof IRateLimitService) {
                List<RateLimitDto> rateLimitList= ((IRateLimitService)bean).getRateLimitList();//加载栏目数据
                if(rateLimitList!=null&&rateLimitList.size()>0){
                    RateLimitMapBean.RATELIMITMAPSTATIC=new HashedMap();
                    for(RateLimitDto rateLimitDto:rateLimitList){
                        RateLimiter rateLimiter=RateLimiter.create(rateLimitDto.getRate());
                        RateLimitMapBean.RATELIMITMAPSTATIC.put(rateLimitDto.getBussinessType(),rateLimiter);
                    }
                }
            }
//            else if(bean instanceof TradeServiceImpl){
//                ((TradeServiceImpl)bean).getTradeList();//加载行业数据
//            }
        } catch (Exception e) {
            logger.error("加载流量控制初始监听器异常!",e);
        }
        return bean;
    }
}

