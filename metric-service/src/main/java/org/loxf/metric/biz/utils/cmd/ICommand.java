package org.loxf.metric.biz.utils.cmd;

/**
 * Created by luohj on 2017/5/22.
 */
public interface ICommand {
    /**
     * @param content
     * @param object
     * @return
     */
    public Object exe(String content, Object object);
}
