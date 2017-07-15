package org.loxf.metric.service.utils;

import org.apache.commons.lang.StringUtils;
import org.loxf.metric.common.dto.Condition;
import org.loxf.metric.common.dto.ConditionVo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luohj on 2017/7/10.
 */
public class ConditionUtil {
    public static void parseCondition(String filterStr, ConditionVo vo) {
        if (StringUtils.isEmpty(filterStr)) {
            return;
        }
        String[] condStrArr = filterStr.split("&");
        List<Condition> conditionList = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (String cond : condStrArr) {
            if (cond.indexOf("relation") > -1) {
                vo.setRelation(cond.split("=")[1]);
            } else {
                if (cond.indexOf("condition.code") > -1) {
                    if (conditionList.size() > 0) {
                        Condition preCondition = conditionList.get(conditionList.size() - 1);
                        preCondition.setValue(values.toArray(new String[values.size()]));
                        values.clear();
                    }
                    Condition condition = new Condition(cond.split("=")[1], null);
                    // 获取条件的名称
                    conditionList.add(condition);
                } else if (cond.indexOf("condition.oper") > -1) {
                    Condition currentCondition = conditionList.get(conditionList.size() - 1);
                    String oper = cond.split("=")[1];
                    if ("eq".equals(oper)) {
                        oper = "=";
                    } else if ("<>".equals(oper)) {
                        oper = "<>";
                    } else if ("not_in".equals(oper)) {
                        oper = "not in";
                    }
                    currentCondition.setOper(oper);
                } else if (cond.indexOf("condition.value") > -1) {
                    Condition currentCondition = conditionList.get(conditionList.size() - 1);
                    String[] tmp = cond.split("=");
                    if(tmp==null || tmp.length==0|| tmp.length==1) {
                        values.add("");
                    } else {
                        try {
                            values.add(URLDecoder.decode(tmp[1], "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            values.add(tmp[1]);
                        }
                    }
                }
            }
        }
        if (conditionList.size() > 0) {
            Condition preCondition = conditionList.get(conditionList.size() - 1);
            preCondition.setValue(values.toArray(new String[values.size()]));
        }
        vo.setCondition(conditionList);
    }

}
