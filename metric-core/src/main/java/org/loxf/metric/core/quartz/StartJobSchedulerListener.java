package org.loxf.metric.core.quartz;

import org.quartz.listeners.SchedulerListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by luohj on 2017/5/4.
 */
public class StartJobSchedulerListener extends SchedulerListenerSupport {
    private static Logger logger = LoggerFactory.getLogger(StartJobSchedulerListener.class);

    @Override
    public void schedulerStarted() {
        logger.debug("Scheduler启动完成...");
    }
}
