package org.loxf.metric.controller;

import com.google.common.util.concurrent.RateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hutingting on 2017/7/22.
 */
public class RateLimitMapBean {
    public static ConcurrentHashMap<String,RateLimiter> RATELIMITMAPSTATIC;
}
