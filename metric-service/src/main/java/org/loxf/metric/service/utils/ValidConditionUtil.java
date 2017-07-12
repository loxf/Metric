package org.loxf.metric.service.utils;

import org.apache.commons.collections.map.HashedMap;
import org.loxf.metric.base.ItemList.QuotaDimItem;
import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.common.dto.Condition;
import org.loxf.metric.common.dto.ConditionVo;
import org.loxf.metric.dal.po.Quota;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by luohj on 2017/5/9.
 */
@Component
public class ValidConditionUtil {

    public Quota valid(String quotaCode, ConditionVo vo) throws MetricException{
        if(StringUtils.isEmpty(quotaCode)){
            throw new MetricException("指标CODE为空");
        }
        if(StringUtils.isEmpty(vo.getBusiDomain())){
            throw new MetricException("指标业务域为空");
        }
        Map<String,Object> qryParams=new HashedMap();
        qryParams.put("quotaCode",quotaCode);
        Quota quota = null;// TODO quotaManager.getQuotaByParams(qryParams);
        if(quota==null) {
            throw new MetricException("指标CODE不正确");
        }
        if(vo.getCondition()!=null){
            List<QuotaDimItem> dimensions = quota.getQuotaDim();
            if(dimensions==null){
                // throw new MetricException("指标未配置过滤条件");
            } else {
                for(Condition con : vo.getCondition()) {
                    boolean isFound = false;
                    for(QuotaDimItem quotaDimension : dimensions){
                        if(quotaDimension.getDimCode().equals(con.getCode())){
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
