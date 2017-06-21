package org.loxf.metric.biz.utils;

import org.apache.commons.lang.StringUtils;
import org.loxf.metric.common.constants.QuotaType;
import org.loxf.metric.common.dto.Condition;
import org.loxf.metric.common.dto.ConditionVo;
import org.loxf.metric.common.dto.GroupBy;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by luohj on 2017/5/10.
 */
public class QuotaSqlBuilder {

    public static String getSql(String quotaCode, String tableName, String expression, ConditionVo vo) {
        StringBuilder sb = new StringBuilder();
        String groupBy = "";
        sb.append("select ");
        expression = expression.replace(getQuotaExpress(quotaCode),  "value");
        sb.append(expression).append(" as value ");
        if (vo.getGroupBy() != null) {
            groupBy = StringUtils.join(vo.getGroupBy().toArray(), ",");
        }
        if (StringUtils.isNotEmpty(groupBy)) {
            sb.append(",").append(groupBy);
        }
        sb.append(" from ").append(tableName).append(" where circle_time between str_to_date('").
                append(vo.getStartCircleTime()).append("','%Y-%m-%d %T') and str_to_date('").
                append(vo.getEndCircleTime()).append("','%Y-%m-%d %T')").append(" and busi_domain = '").
                append(vo.getBusiDomain()).append("' ");
        if (CollectionUtils.isNotEmpty(vo.getCondition())) {
            List<Condition> conditionList = vo.getCondition();
            sb.append(" and (");
            boolean isFirstCondition = true;
            for (Condition cond : conditionList) {
                if (StringUtils.isNotEmpty(cond.getCode()) && cond.getValue()!=null) {
                    if(isFirstCondition){
                        sb.append(convertCondition(cond));
                        isFirstCondition = false;
                    } else {
                        sb.append(" ").append(vo.getRelation()).append(" ").append(convertCondition(cond));
                    }
                }
            }
            sb.append(") ");
        }
        if (StringUtils.isNotEmpty(groupBy)) {
            sb.append("group by ").append(groupBy);
        }
        return sb.toString();
    }

    public static String getSql(Map tableMap, String oper, String expression, ConditionVo vo, String type) {
        StringBuilder sb = new StringBuilder();
        Iterator ite = tableMap.keySet().iterator();
        Map<String, String> alisa = new HashMap<String, String>();
        int i=1;
        while (ite.hasNext()){
            String tableKey = (String)ite.next();
            String alisaTableName = "T" + i++;
            alisa.put(tableKey, alisaTableName);
            if(QuotaType.COMPOSITE.getValue().equals(type)) {
                expression = expression.replace(getQuotaExpress(tableKey), alisaTableName + ".value ");
            } else {
                expression = alisaTableName + ".value ";
            }
        }
        String groupBy = "";
        String operStr = (StringUtils.isEmpty(oper)) || oper.startsWith("LIST")? "" : oper;
        String []command = operStr.split(" ");
        String op = "IFNULL(${expression}, 0) as value";
        if(command.length>0 && StringUtils.isNotEmpty(command[0])){
            op = command[0] + "(${expression}) as value";
        }
        if(QuotaType.BASIC.getValue().equals(type) ) {
            sb.append("select ").append(op.replace("${expression}", expression));
        } else {
            sb.append("select ").append("IFNULL(").append(expression).append(", 0) as value");
        }
        if(QuotaType.COMPOSITE.getValue().equals(type) && vo.getGroupBy() != null){
            // 复合指标有维度的时候，需要算维度值
            for(GroupBy gr : vo.getGroupBy()) {
                groupBy += ",T1." + gr.getCode();
            }
        }
        if (StringUtils.isNotEmpty(groupBy)) {
            sb.append(groupBy);
        }
        sb.append(" from ");
        Iterator aliasIte = alisa.keySet().iterator();
        String [] aliasCond = new String[alisa.size()];
        int j = 0;
        while (aliasIte.hasNext()){
            if(j>0){
                sb.append(", ");
            }
            String tableKey = (String)aliasIte.next();
            String aliasName = alisa.get(tableKey);
            aliasCond[j++] = aliasName;
            sb.append("(").append(tableMap.get(tableKey)).append(") ").append(aliasName);
        }
        // where 条件拼接
        sb.append(" where 1=1 ");
        if (vo.getGroupBy()!= null) {
            for(GroupBy gr : vo.getGroupBy()) {
                for (int m = 1; m < aliasCond.length; m++) {
                    sb.append(" and ").append(aliasCond[m - 1]).append(".").append(gr.getCode()).append("=").
                            append(aliasCond[m]).append(".").append(gr.getCode());
                }
            }
        }
        if(QuotaType.COMPOSITE.getValue().equals(type) && StringUtils.isNotEmpty(op) && StringUtils.isNotEmpty(oper) ) {
            // 复合指标 且 有展现函数的时候（求复合指标指标值）
            sb.append(") m");
            sb.insert(0, "select " + op.replace("${expression}", "m.value")+ " from (");
        }
        return sb.toString();
    }

    public static boolean isQuotaExpress(String str) {
        if (org.apache.commons.lang.StringUtils.isBlank(str) || str.length() < 4) {
            return false;
        }
        if (str.startsWith("${") && str.endsWith("}")) {
            str = str.substring(2, str.length() - 1);
            if (str.indexOf("{") > 0 || str.indexOf("}") > 0 || str.indexOf("$") > 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static String getQuotaExpress(String str) {
        if (str.indexOf("{") > 0 || str.indexOf("}") > 0 || str.indexOf("$") > 0) {
            return null;
        } else {
            return "${" + str + "}";
        }
    }

    public static List<String> quotaList(String str){
        List result = new ArrayList<String>();
        String regEx = "\\$\\{([\\s\\S].*?)\\}";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(str);
        while (mat.find()){
            String tmp = mat.group();
            result.add(tmp.substring(2, tmp.length()-1));
        }
        return result;
    }

    public static String convertCondition(Condition condition){
        StringBuilder sql = new StringBuilder();
        sql.append(condition.getCode()).append(" ");
        if(condition.getOper().equals("≠")) {
            sql.append("<>");
        } else {
            sql.append(condition.getOper());
        }
        String [] values = condition.getValue();
        String valueStr = "";
        if(values!=null && values.length>0) {
            String[] tmp = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                tmp[i] = "'" + values[i] + "'";
            }
            valueStr = StringUtils.join(tmp, ",");;
        } else {
            valueStr = "''";
        }
        sql.append(" (").append(valueStr).append(") ");
        return sql.toString();
    }
}
