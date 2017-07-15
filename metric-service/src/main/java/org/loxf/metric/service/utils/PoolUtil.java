package org.loxf.metric.service.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by luohj on 2017/7/15.
 */
public class PoolUtil {
    private static ExecutorService pool;
    public static ExecutorService getPool(){
        if(pool==null) {
            synchronized (PoolUtil.class) {
                if (pool == null) {
                    pool = Executors.newFixedThreadPool(500);
                }
            }
        }
        return pool;
    }
}
