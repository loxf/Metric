package org.loxf.metric.biz.utils;

import org.loxf.metric.common.constants.QuotaType;
import org.loxf.metric.common.dto.ConditionVo;
import org.loxf.metric.dal.po.Quota;
import org.loxf.metric.service.QuotaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 指标的运算
 * Created by luohj on 2017/5/9.
 */
@Component
public class QuotaOperation {
    @Autowired
    private QuotaManager mgr;

    /**
     * 获取基础指标的sql
     * @param code
     * @param vo
     * @return
     */
    public String getSql(String code, ConditionVo vo) {
        Quota quota = mgr.getQuotaByCode(code);
        if (QuotaType.BASIC.getValue().equals(quota.getType())) {
            return QuotaSqlBuilder.getSql(quota.getQuotaCode(), quota.getQuotaSource(), quota.getExpression(), vo);
        }
        return null;
    }

    /**
     * 获取指标的最终展现sql
     * @param quotaSql
     * @param expression
     * @param type CHART / QUOTA
     * @param condition
     * @return
     */
    public String getSql(Map quotaSql, String expression, String type, ConditionVo condition){
        Quota quota = mgr.getQuotaByCode(condition.getQuotaCode());
        if(QuotaType.BASIC.getValue().equals(quota.getType()) && "QUOTA".equals(type)){// 基础指标的指标值
            if(quota.getShowOperation()!=null && quota.getShowOperation().startsWith("LIST")){
                return quotaSql.get(quota.getQuotaCode()).toString();
            }
            return QuotaSqlBuilder.getSql(quotaSql, quota.getShowOperation(), expression, condition, QuotaType.BASIC.getValue());
        } else if(QuotaType.BASIC.getValue().equals(quota.getType()) && "CHART".equals(type)){// 基础指标的图
            return (String)quotaSql.get(quota.getQuotaCode());
        } else if(QuotaType.COMPOSITE.getValue().equals(quota.getType()) && "QUOTA".equals(type)) {// 复合指标的指标值
            if(quota.getShowOperation()!=null && quota.getShowOperation().startsWith("LIST")){
                return quotaSql.get(quota.getQuotaCode()).toString();
            }
            return QuotaSqlBuilder.getSql(quotaSql, quota.getShowOperation(), expression, condition, QuotaType.COMPOSITE.getValue());
        } else if(QuotaType.COMPOSITE.getValue().equals(quota.getType()) && "CHART".equals(type)) {// 复合指标的图
            return QuotaSqlBuilder.getSql(quotaSql, null, expression, condition, QuotaType.COMPOSITE.getValue());
        } else {
            return null;
        }
    }

    public List<Map> query(String sql){
        return mgr.queryBySql(sql);
    }

    public static void main(String[] args){
        ConditionVo vo = new ConditionVo();
        vo.setStartCircleTime("2017-04-15");
        vo.setEndCircleTime("2017-05-15");
        /*List<GroupBy> groupBy = new ArrayList<>();
        groupBy.add(new GroupBy("ADVERT_NAME"));
        vo.setGroupBy(groupBy);*/

        List<Quota> quotaList = new ArrayList<>();
        Quota quota = new Quota();
        quota.setQuotaCode("CHARGE_MONEY");
        quota.setQuotaSource("quota_data_charge_money");
        quota.setExpression("${CONSUME_MONEY}");
        System.out.println(QuotaSqlBuilder.getSql(quota.getQuotaCode(), quota.getQuotaSource(), quota.getExpression(), vo));
    }
}
