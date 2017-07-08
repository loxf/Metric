package org.loxf.metric.biz.utils.cmd;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/5/22.
 */
public class MAXCommand implements ICommand {
    private static Logger logger = LoggerFactory.getLogger(MAXCommand.class);
    @Override
    public Object exe(String content, Object object) {
        if(object ==null){
            return "0";
        }
        if(StringUtils.isEmpty(content)){
            content = "value";
        }
        try {
            if (object instanceof String || object instanceof Double || object instanceof Map) {
                return object;
            } else if (object instanceof List) {
                BigDecimal value = null;
                for (Map o : (List<Map>) object) {
                    if(o.get(content)!=null) {
                        if(value==null) {
                            value = new BigDecimal(o.get(content).toString());
                        } else if (value.compareTo(new BigDecimal(o.get(content).toString()))<0){
                            value = new BigDecimal(o.get(content).toString());
                        }
                    }
                }
                return value;
            }
        } catch (Exception e){
            logger.error("-MAX命令执行错误，命令内容：" + content + " ，执行对象：" + object, e);
        }
        return object;
    }
}
