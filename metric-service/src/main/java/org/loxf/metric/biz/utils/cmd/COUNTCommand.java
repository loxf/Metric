package org.loxf.metric.biz.utils.cmd;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/5/22.
 */
public class COUNTCommand implements ICommand {
    private static Logger logger = LoggerFactory.getLogger(COUNTCommand.class);
    @Override
    public Object exe(String content, Object object) {
        if(object ==null){
            return "0";
        }
        if(StringUtils.isEmpty(content)){
            content = "CIRCLE_TIME";
        }
        try {
            if (object instanceof List) {
                int count = 0;
                HashSet hashSet = new HashSet();
                for (Map o : (List<Map>) object) {
                    Object contentValue = o.get(content);// 求和列的内容
                    hashSet.add(contentValue);
                }
                return hashSet.size();
            } else if (object instanceof Collection) {
                return ((Collection) object).size();
            } else if (object instanceof Map) {
                return ((Map) object).size();
            } else {
                return 1;
            }
        } catch (Exception e){
            logger.error("-SUM命令执行错误，命令内容：" + content + " ，执行对象：" + object, e);
        }
        return null;
    }
}
