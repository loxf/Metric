package org.loxf.metric.velocity;

import org.apache.commons.lang.StringUtils;

/**
 * Created by luohj on 2017/3/31.
 */
public class VelocityCommonTool {
    public String[] split(String str, String separator){
        if(StringUtils.isEmpty(str)){
            return new String[0];
        } else {
            return str.split(separator);
        }
    }
}
