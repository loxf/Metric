package org.loxf.metric.service.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.loxf.metric.common.dto.Condition;
import org.loxf.metric.common.dto.ConditionVo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by luohj on 2017/5/10.
 */
public class QuotaSqlBuilder {

    public static String getSql(String quotaCode, String tableName, boolean forQuotaScan, String showOperation, ConditionVo vo) {
        StringBuilder sb = new StringBuilder();
        String groupBy = "";
        String oper = showOperation.split(" ")[0];
        // expression = expression.replace(getQuotaExpress(quotaCode),  "");
        // COUNT 类型 且 为指标展示时 需要特殊处理
        if(forQuotaScan && "COUNT".equals(oper)){
            sb.append("select COUNT(DISTINCT(value)) as value ");
        } else {
            sb.append("select ").append(("COUNT".equals(oper)?"COUNT(DISTINCT(value)) as value": oper + "(CONVERT(value,decimal)) as value"));
            if (vo.getGroupBy() != null) {
                groupBy = StringUtils.join(vo.getGroupBy().toArray(), ",");
            }
            if (StringUtils.isNotEmpty(groupBy)) {
                sb.append(",").append(groupBy);
            }
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
