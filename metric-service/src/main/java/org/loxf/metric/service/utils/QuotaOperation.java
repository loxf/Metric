package org.loxf.metric.service.utils;

import org.loxf.metric.common.constants.QuotaType;
import org.loxf.metric.common.dto.ConditionVo;
import org.loxf.metric.common.dto.QuotaDataList;
import org.loxf.metric.dal.po.Quota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 指标的运算
 * Created by luohj on 2017/5/9.
 */
@Component("quotaOperation")
public class QuotaOperation {

    /**
     * 获取基础指标的sql
     * @param code
     * @param forQuotaScan
     * @param vo
     * @return
     */
    public String getSql(String code, boolean forQuotaScan, ConditionVo vo) {
        /*Quota quota = mgr.getQuotaByCode(code);
        if (QuotaType.BASIC.getValue().equals(quota.getType())) {
            return QuotaSqlBuilder.getSql(quota.getQuotaCode(), quota.getQuotaSource(), forQuotaScan, quota.getShowOperation(), vo);
        }*/
        return null;
    }

    public QuotaDataList query(String sql){
        /*return mgr.queryBySql(sql);*/
        return null;
    }

    public Quota getQuota(String code){
        /*return mgr.getQuotaByCode(code);*/
        return null;
    }

}
