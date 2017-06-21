package org.loxf.metric.biz.utils.cmd;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by luohj on 2017/5/22.
 */
public class SUMCommand implements ICommand {
    private static Logger logger = LoggerFactory.getLogger(SUMCommand.class);
    @Override
    public Object exe(String content, Object object) {
        if(object ==null){
            return "0";
        }
        if(StringUtils.isEmpty(content)){
            content = "CIRCLE_TIME";
        }
        try {
            if (object instanceof String || object instanceof Double || object instanceof Map) {
                return object;
            } else if (object instanceof List) {
                List<Map> result = new ArrayList<>();
                BigDecimal value = new BigDecimal(0);
                if(!content.equalsIgnoreCase("value")){
                    Map temp = new LinkedHashMap();
                    for (Map o : (List<Map>) object) {
                        Object contentValue = o.get(content);// 求和列的内容
                        // 初始化当前值
                        if(temp.get(contentValue)!=null){
                            value = new BigDecimal(temp.get(contentValue).toString());
                        } else {
                            value = new BigDecimal(0);
                        }
                        temp.put(contentValue, new BigDecimal(o.get("value").toString()).add(value));// 加上新的值
                    }
                    Iterator ite = temp.keySet().iterator();
                    while (ite.hasNext()){
                        Map tmp = new HashMap();
                        Object key = ite.next();
                        tmp.put("value", temp.get(key));
                        tmp.put(content, key);
                        result.add(tmp);
                    }
                } else {
                    for (Map o : (List<Map>) object) {
                        value = new BigDecimal(o.get("value").toString()).add(value);
                    }
                    return value;
                }
                return result;
            }
        } catch (Exception e){
            logger.error("-SUM命令执行错误，命令内容：" + content + " ，执行对象：" + object, e);
        }
        return object;
    }
}
