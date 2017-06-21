package org.loxf.metric.biz.utils.cmd;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/5/22.
 */
public class FORMATCommand implements ICommand {
    private static Logger logger = LoggerFactory.getLogger(FORMATCommand.class);
    @Override
    public Object exe(String content, Object object) {
        if(object ==null){
            return "0";
        }
        if(StringUtils.isEmpty(content)){
            content = "";
        }
        try {
            if (object instanceof String || object instanceof Integer || object instanceof Double ) {
                return object + content;
            } else if(object instanceof Map){
                return ((Map) object).get("value") + content;
            }else if (object instanceof List) {
                int count = 0;
                HashSet hashSet = new HashSet();
                for (Map o : (List<Map>) object) {
                    Object value = o.get("value");// 求和列的内容
                    o.put("value", value + content);
                }
                return object;
            }
            return object.toString() + content;
        } catch (Exception e){
            logger.error("-SUM命令执行错误，命令内容：" + content + " ，执行对象：" + object, e);
        }
        return null;
    }
}
