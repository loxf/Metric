package org.loxf.metric.service.utils.cmd;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/5/22.
 */
public class RCommand implements ICommand {
    private static Logger logger = LoggerFactory.getLogger(RCommand.class);

    @Override
    public Object exe(String content, Object object) {
        if (object == null) {
            return "0";
        }
        if (StringUtils.isEmpty(content)) {
            content = "0";
        }
        try {
            if (object instanceof String || object instanceof Double) {
                BigDecimal bigDecimal = new BigDecimal(object.toString()).setScale(Integer.valueOf(content), BigDecimal.ROUND_HALF_UP);
                return bigDecimal.toString();
            } else if (object instanceof List) {
                for (Map o : (List<Map>) object) {
                    BigDecimal bigDecimal = new BigDecimal(o.get("value").toString()).setScale(Integer.valueOf(content), BigDecimal.ROUND_HALF_UP);
                    o.put("value", bigDecimal.toString());
                }
                return object;
            } else if (object instanceof Map) {
                BigDecimal bigDecimal = new BigDecimal(((Map) object).get("value").toString()).setScale(Integer.valueOf(content), BigDecimal.ROUND_HALF_UP);
                return bigDecimal.toString();
            }
        } catch (Exception e) {
            logger.error("-R命令执行错误，命令内容：" + content + " ，执行对象：" + object, e);
        }
        return object;
    }
}
