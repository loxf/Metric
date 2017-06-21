package org.loxf.metric.biz.utils;

import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.common.dto.Condition;
import org.loxf.metric.common.dto.ConditionVo;
import org.loxf.metric.dal.po.Quota;
import org.loxf.metric.dal.po.QuotaDimension;
import org.loxf.metric.service.QuotaManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by luohj on 2017/5/9.
 */
@Component
public class ValidConditionUtil {
    @Autowired
    private QuotaManager quotaManager;

    public Quota valid(String quotaCode, ConditionVo vo) throws MetricException{
        if(StringUtils.isEmpty(quotaCode)){
            throw new MetricException("指标CODE为空");
        }
        if(StringUtils.isEmpty(vo.getBusiDomain())){
            throw new MetricException("指标业务域为空");
        }
        Quota quota = quotaManager.getQuotaByCode(quotaCode);
        if(quota==null) {
            throw new MetricException("指标CODE不正确");
        }
        if(vo.getCondition()!=null){
            List<QuotaDimension> dimensions = quota.getQuotaDimensionList();
            if(dimensions==null){
                // throw new MetricException("指标未配置过滤条件");
            } else {
                for(Condition con : vo.getCondition()) {
                    boolean isFound = false;
                    for(QuotaDimension quotaDimension : dimensions){
                        if(quotaDimension.getColumnCode().equals(con.getCode())){
                            isFound = true;
                            break;
                        }
                    }
                    if(!isFound){
                        throw new MetricException("指标未配置当前条件：" + con.getCode(), "CHECK_CONDITION_01");
                    }
                }
            }
        }
        return quota;
    }
}
