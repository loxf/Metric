package org.loxf.metric.biz.utils.cmd;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.common.dto.QuotaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/5/22.
 */
public class MINCommand implements ICommand {
    private static Logger logger = LoggerFactory.getLogger(MINCommand.class);
    @Override
    public Object exe(String content, Object object) {
        if(object ==null){
            return "0";
        }
        if(StringUtils.isEmpty(content)){
            content = "value";
        }
        try {
            if (object instanceof QuotaData || object instanceof String || object instanceof Double || object instanceof Map) {
                return object;
            } else if (object instanceof List) {
                if(content.equals("value")) {
                    if(CollectionUtils.isNotEmpty((List<QuotaData>)object)) {
                        BigDecimal value = null;
                        for (QuotaData o : (List<QuotaData>) object) {
                            BigDecimal tmp = o.getValue();
                            if (tmp != null && (value==null ||value.compareTo(tmp) > 0)) {
                                value = tmp;
                            }
                        }
                        return value;
                    }
                } else {
                    // 求其他维度的最小值未实现
                }
                return BigDecimal.ZERO;
            }
        } catch (Exception e){
            logger.error("-MIN命令执行错误，命令内容：" + content + " ，执行对象：" + object, e);
        }
        return object;
    }
}
